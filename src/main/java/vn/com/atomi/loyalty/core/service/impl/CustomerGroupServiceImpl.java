package vn.com.atomi.loyalty.core.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.base.utils.RequestUtils;
import vn.com.atomi.loyalty.core.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.core.dto.input.CustomerGroupInput;
import vn.com.atomi.loyalty.core.dto.output.*;
import vn.com.atomi.loyalty.core.enums.ApprovalStatus;
import vn.com.atomi.loyalty.core.enums.ApprovalType;
import vn.com.atomi.loyalty.core.enums.ErrorCode;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.feign.LoyaltyConfigClient;
import vn.com.atomi.loyalty.core.repository.CustomerGroupApprovalRepository;
import vn.com.atomi.loyalty.core.repository.CustomerGroupRepository;
import vn.com.atomi.loyalty.core.service.CustomerGroupService;
import vn.com.atomi.loyalty.core.utils.Utils;

/**
 * @author haidv
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class CustomerGroupServiceImpl extends BaseService implements CustomerGroupService {

  private final CustomerGroupRepository customerGroupRepository;

  private final CustomerGroupApprovalRepository customerGroupApprovalRepository;

  private final LoyaltyConfigClient loyaltyConfigClient;

  @Override
  public void createCustomerGroup(CustomerGroupInput customerGroupInput) {}

  @Override
  public ResponsePage<CustomerGroupApprovalPreviewOutput> getCustomerGroupApprovals(
      Status status,
      ApprovalStatus approvalStatus,
      ApprovalType approvalType,
      String startApprovedDate,
      String endApprovedDate,
      String name,
      String code,
      Pageable pageable) {
    LocalDateTime edDate = Utils.convertToLocalDateTimeEndDay(endApprovedDate);
    LocalDateTime stDate = Utils.convertToLocalDateTimeStartDay(startApprovedDate);
    // nếu tìm kiếm theo khoảng ngày phê duyệt thì trạng thái phê duyệt phải là đồng ý hoặc từ chối
    if ((stDate != null || edDate != null)
        && (ApprovalStatus.WAITING.equals(approvalStatus)
            || ApprovalStatus.RECALL.equals(approvalStatus))) {
      return new ResponsePage<>(
          pageable.getPageNumber(), pageable.getPageSize(), 0, 0, new ArrayList<>());
    }
    var customerGroupApprovalPage =
        customerGroupApprovalRepository.findByCondition(
            status,
            approvalStatus,
            approvalType,
            Utils.makeLikeParameter(name),
            Utils.makeLikeParameter(code),
            stDate,
            edDate,
            pageable);
    if (!CollectionUtils.isEmpty(customerGroupApprovalPage.getContent())) {
      return new ResponsePage<>(
          customerGroupApprovalPage,
          super.modelMapper.convertToCustomerGroupApprovalPreviewOutputs(
              customerGroupApprovalPage.getContent()));
    }
    return new ResponsePage<>(customerGroupApprovalPage, new ArrayList<>());
  }

  @Override
  public CustomerGroupApprovalOutput getCustomerGroupApproval(Long id) {
    var customerGroupApproval =
        customerGroupApprovalRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.CUSTOMER_GROUP_NOT_EXISTED));
    return super.modelMapper.convertCustomerGroupApprovalOutput(customerGroupApproval);
  }

  @Override
  public List<ComparisonOutput> getCustomerGroupApprovalComparison(Long id) {
    return null;
  }

  @Override
  public void recallCustomerGroupApproval(Long id) {
    // chỉ được thu hồi những bản ghi ở trạng thái chờ duyệt
    customerGroupApprovalRepository
        .findByDeletedFalseAndIdAndApprovalStatus(id, ApprovalStatus.WAITING)
        .ifPresentOrElse(
            customerGroupApproval -> {
              customerGroupApproval.setApprovalStatus(ApprovalStatus.RECALL);
              customerGroupApprovalRepository.save(customerGroupApproval);
            },
            () -> {
              throw new BaseException(ErrorCode.APPROVING_RECORD_NOT_EXISTED);
            });
  }

  @Override
  public ResponsePage<CustomerGroupPreviewOutput> getCustomerGroups(
      Status status, String name, String code, Pageable pageable) {
    var customerGroups =
        customerGroupRepository.findByCondition(
            status, Utils.makeLikeParameter(name), Utils.makeLikeParameter(code), pageable);
    if (!CollectionUtils.isEmpty(customerGroups.getContent())) {
      return new ResponsePage<>(
          customerGroups,
          super.modelMapper.convertToCustomerGroupPreviewOutputs(customerGroups.getContent()));
    }
    return new ResponsePage<>(customerGroups, new ArrayList<>());
  }

  @Override
  public CustomerGroupOutput getCustomerGroup(Long id) {
    var customerGroup =
        customerGroupRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.CUSTOMER_GROUP_NOT_EXISTED));
    return super.modelMapper.convertCustomerGroupOutput(customerGroup);
  }

  @Override
  public void updateCustomerGroup(Long id, CustomerGroupInput customerGroupInput) {
    // lấy nhóm khách hàng hiện tại
    var customerGroup =
        customerGroupRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.CUSTOMER_GROUP_NOT_EXISTED));
    // kiểm tra xem có chiến dịch nào sử dụng nhóm khách hàng này đang hoạt động hoặc sắp hoạt động
    // không
    if (Boolean.TRUE.equals(
        loyaltyConfigClient.checkCampaignActive(RequestUtils.extractRequestId(), id).getData())) {
      throw new BaseException(ErrorCode.EXISTED_CAMPAIGN_USE_CUSTOMER_GROUP);
    }
    // map data mới vào nhóm khách hàng hiện tại
    var newCustomerGroup =
        super.modelMapper.convertToCustomerGroup(customerGroup, customerGroupInput);
    // tạo bản ghi chờ duyệt
    var customerGroupApproval =
        super.modelMapper.convertToGroupApproval(
            newCustomerGroup, ApprovalStatus.WAITING, ApprovalType.UPDATE);
    customerGroupApprovalRepository.save(customerGroupApproval);
  }

  @Override
  public void approveCustomerGroup(ApprovalInput input) {
    // tìm kiếm bản ghi chờ duyệt
    var customerGroupApproval =
        customerGroupApprovalRepository
            .findByDeletedFalseAndIdAndApprovalStatus(input.getId(), ApprovalStatus.WAITING)
            .orElseThrow(() -> new BaseException(ErrorCode.APPROVING_RECORD_NOT_EXISTED));
    if (input.getIsAccepted()) {
      // trường hợp phê duyệt tạo
      if (ApprovalType.CREATE.equals(customerGroupApproval.getApprovalType())) {
        // lưu thông tin nhóm khách hàng
        var customerGroup = super.modelMapper.convertToCustomerGroup(customerGroupApproval);
        customerGroup = customerGroupRepository.save(customerGroup);
        customerGroupApproval.setCustomerGroupId(customerGroup.getId());
      }
      // trường hợp phê duyệt cập nhật
      if (ApprovalType.UPDATE.equals(customerGroupApproval.getApprovalType())) {
        // lấy thông tin nhóm khách hàng hiện tại
        var currentGroup =
            customerGroupRepository
                .findByDeletedFalseAndId(customerGroupApproval.getCustomerGroupId())
                .orElseThrow(() -> new BaseException(ErrorCode.CUSTOMER_GROUP_NOT_EXISTED));
        // kiểm tra xem có chiến dịch nào sử dụng nhóm khách hàng này đang hoạt động hoặc sắp hoạt
        // động không
        if (Boolean.TRUE.equals(
            loyaltyConfigClient
                .checkCampaignActive(RequestUtils.extractRequestId(), currentGroup.getId())
                .getData())) {
          throw new BaseException(ErrorCode.EXISTED_CAMPAIGN_USE_CUSTOMER_GROUP);
        }
        currentGroup =
            super.modelMapper.convertToCustomerGroup(currentGroup, customerGroupApproval);
        customerGroupRepository.save(currentGroup);
      }
    }
    // cập nhật trạng thái bản ghi chờ duyệt
    customerGroupApproval.setApprovalStatus(
        input.getIsAccepted() ? ApprovalStatus.ACCEPTED : ApprovalStatus.REJECTED);
    customerGroupApproval.setApprovalComment(input.getComment());
    customerGroupApprovalRepository.save(customerGroupApproval);
  }

  @Override
  public Boolean checkCustomerGroupExisted(Long id) {
    return customerGroupRepository.existsByIdAndDeletedFalse(id);
  }

  @Override
  public List<InternalCustomerGroupOutput> getInternalCustomerGroup(
      Long customerId, String cifBank, String cifWallet) {
    return null;
  }
}
