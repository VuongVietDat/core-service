package vn.com.atomi.loyalty.core.service.impl;

import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.base.utils.RequestUtils;
import vn.com.atomi.loyalty.core.dto.output.CustomerPointAccountOutput;
import vn.com.atomi.loyalty.core.dto.output.CustomerPointAccountPreviewOutput;
import vn.com.atomi.loyalty.core.enums.ErrorCode;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.feign.LoyaltyConfigClient;
import vn.com.atomi.loyalty.core.repository.CustomerBalanceHistoryRepository;
import vn.com.atomi.loyalty.core.repository.CustomerRepository;
import vn.com.atomi.loyalty.core.service.CustomerService;

/**
 * @author haidv
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl extends BaseService implements CustomerService {

  private final CustomerRepository customerRepository;

  private final CustomerBalanceHistoryRepository customerBalanceHistoryRepository;

  private final LoyaltyConfigClient loyaltyConfigClient;

  @Override
  public CustomerPointAccountOutput getCustomerPointAccount(Long customerId) {
    var pointAccountProjection = customerRepository.findByDeletedFalseAndPointAccount(customerId);
    if (pointAccountProjection == null || pointAccountProjection.getCustomerId() == null) {
      throw new BaseException(ErrorCode.CUSTOMER_NOT_EXISTED);
    }
    // lấy danh sách rank để map tên rank
    var ranks = loyaltyConfigClient.getAllRanks(RequestUtils.extractRequestId()).getData();
    var out = super.modelMapper.convertToCustomerPointAccountOutput(pointAccountProjection, ranks);
    // lấy điểm gần hết hạn
    customerBalanceHistoryRepository
        .findPointAboutExpire(customerId)
        .ifPresent(
            customerBalanceHistory -> {
              out.setPointAboutExpire(
                  customerBalanceHistory.getAmount() - customerBalanceHistory.getAmountUsed());
              out.setMostRecentExpirationDate(customerBalanceHistory.getExpireAt());
            });
    return out;
  }

  @Override
  public ResponsePage<CustomerPointAccountPreviewOutput> getCustomerPointAccounts(
      Status status,
      Long customerId,
      String customerName,
      String cifBank,
      String cifWallet,
      String uniqueValue,
      Long pointFrom,
      Long pointTo,
      String rank,
      Pageable pageable) {
    var pointAccountProjectionPage =
        customerRepository.findByDeletedFalseAndPointAccount(
            status,
            customerId,
            customerName,
            cifBank,
            cifWallet,
            uniqueValue,
            rank,
            pointFrom,
            pointTo,
            pageable);
    if (!CollectionUtils.isEmpty(pointAccountProjectionPage.getContent())) {
      // lấy danh sách rank để map tên rank
      var ranks = loyaltyConfigClient.getAllRanks(RequestUtils.extractRequestId()).getData();
      return new ResponsePage<>(
          pointAccountProjectionPage,
          super.modelMapper.convertToCustomerPointAccountPreviewOutput(
              pointAccountProjectionPage.getContent(), ranks));
    }
    return new ResponsePage<>(pointAccountProjectionPage, new ArrayList<>());
  }
}
