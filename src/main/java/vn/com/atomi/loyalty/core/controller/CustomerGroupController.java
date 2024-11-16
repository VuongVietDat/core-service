package vn.com.atomi.loyalty.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.annotations.DateTimeValidator;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.*;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.core.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.core.dto.input.CustomerGroupInput;
import vn.com.atomi.loyalty.core.dto.output.*;
import vn.com.atomi.loyalty.core.enums.ApprovalStatus;
import vn.com.atomi.loyalty.core.enums.ApprovalType;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.service.CustomerGroupService;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class CustomerGroupController extends BaseController {

    private final CustomerGroupService customerGroupService;

    @Operation(summary = "Api tạo mới nhóm khách hàng (bản ghi chờ duyệt)")
    @PreAuthorize(Authority.CustomerGroup.CREATE_CUSTOMER_GROUP)
    @PostMapping("/customer-groups/approvals")
    public ResponseEntity<ResponseData<Void>> createCustomerGroup(
            @Valid @RequestBody CustomerGroupInput customerGroupInput) {
        customerGroupService.createCustomerGroup(customerGroupInput);
        return ResponseUtils.success();
    }

    @Operation(summary = "Api lấy danh sách duyệt nhóm khách hàng")
    @PreAuthorize(Authority.CustomerGroup.READ_CUSTOMER_GROUP)
    @GetMapping("/customer-groups/approvals")
    public ResponseEntity<ResponseData<ResponsePage<CustomerGroupApprovalPreviewOutput>>>
    getCustomerGroupApprovals(
            @Parameter(description = "Số trang, bắt đầu từ 1", example = "1") @RequestParam
            Integer pageNo,
            @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200", example = "10")
            @RequestParam
            Integer pageSize,
            @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
            @RequestParam(required = false)
            String sort,
            @Parameter(
                    description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
            @RequestParam(required = false)
            Status status,
            @Parameter(
                    description =
                            "Trạng thái phê duyệt:</br> WAITING: Chờ duyệt</br> ACCEPTED: Đồng ý</br> REJECTED: Từ chối</br> RECALL: Thu hồi")
            @RequestParam(required = false)
            ApprovalStatus approvalStatus,
            @Parameter(
                    description =
                            "Loại phê duyệt: </br>CREATE: Phê duyệt tạo</br>UPDATE: Phê duyệt cập nhật</br>CANCEL: Phê duyệt hủy bỏ")
            @RequestParam(required = false)
            ApprovalType approvalType,
            @Parameter(description = "Thời gian duyệt từ ngày (dd/MM/yyyy)", example = "01/01/2024")
            @DateTimeValidator(
                    required = false,
                    pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
            @RequestParam(required = false)
            String startApprovedDate,
            @Parameter(description = "Thời gian duyệt đến ngày (dd/MM/yyyy)", example = "31/12/2024")
            @DateTimeValidator(
                    required = false,
                    pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
            @RequestParam(required = false)
            String endApprovedDate,
            @Parameter(description = "Tên nhóm khách hàng") @RequestParam(required = false)
            String name,
            @Parameter(description = "Mã nhóm khách hàng") @RequestParam(required = false)
            String code) {
        return ResponseUtils.success(
                customerGroupService.getCustomerGroupApprovals(
                        status,
                        approvalStatus,
                        approvalType,
                        startApprovedDate,
                        endApprovedDate,
                        name,
                        code,
                        super.pageable(pageNo, pageSize, sort)));
    }

    @Operation(summary = "Api lấy chi tiết bản ghi duyệt nhóm khách hàng theo id")
    @PreAuthorize(Authority.CustomerGroup.READ_CUSTOMER_GROUP)
    @GetMapping("/customer-groups/approvals/{id}")
    public ResponseEntity<ResponseData<CustomerGroupApprovalOutput>> getCustomerGroupApproval(
            @Parameter(description = "ID bản ghi chờ duyệt") @PathVariable Long id) {
        return ResponseUtils.success(customerGroupService.getCustomerGroupApproval(id));
    }

    @Operation(
            summary =
                    "Api so sánh bản ghi duyệt cập nhật hiện tại với bản ghi đã được phê duyệt trước đó")
    @PreAuthorize(Authority.CustomerGroup.READ_CUSTOMER_GROUP)
    @GetMapping("/customer-groups/approvals/{id}/comparison")
    public ResponseEntity<ResponseData<List<ComparisonOutput>>> getCustomerGroupApprovalComparison(
            @Parameter(description = "ID bản ghi duyệt cập nhật") @PathVariable Long id) {
        return ResponseUtils.success(customerGroupService.getCustomerGroupApprovalComparison(id));
    }

    @Operation(summary = "Api thu hồi yêu cầu chờ duyệt nhóm khách hàng theo id")
    @PreAuthorize(Authority.CustomerGroup.UPDATE_CUSTOMER_GROUP)
    @PutMapping("/customer-groups/approvals/{id}/recall")
    public ResponseEntity<ResponseData<Void>> recallCustomerGroupApproval(
            @Parameter(description = "ID bản ghi chờ duyệt") @PathVariable Long id) {
        customerGroupService.recallCustomerGroupApproval(id);
        return ResponseUtils.success();
    }

    @Operation(summary = "Api lấy danh sách nhóm khách hàng")
    @PreAuthorize(Authority.CustomerGroup.READ_CUSTOMER_GROUP)
    @GetMapping("/customer-groups")
    public ResponseEntity<ResponseData<ResponsePage<CustomerGroupPreviewOutput>>> getCustomerGroups(
            @Parameter(description = "Số trang, bắt đầu từ 1", example = "1") @RequestParam
            Integer pageNo,
            @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200", example = "10") @RequestParam
            Integer pageSize,
            @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
            @RequestParam(required = false)
            String sort,
            @Parameter(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
            @RequestParam(required = false)
            Status status,
            @Parameter(description = "Tên nhóm khách hàng") @RequestParam(required = false) String name,
            @Parameter(description = "Mã nhóm khách hàng") @RequestParam(required = false) String code) {
        return ResponseUtils.success(
                customerGroupService.getCustomerGroups(
                        status, name, code, super.pageable(pageNo, pageSize, sort)));
    }

    @Operation(summary = "Api lấy chi tiết bản ghi nhóm khách hàng theo id")
    @PreAuthorize(Authority.CustomerGroup.READ_CUSTOMER_GROUP)
    @GetMapping("/customer-groups/{id}")
    public ResponseEntity<ResponseData<CustomerGroupOutput>> getCustomerGroup(
            @Parameter(description = "ID bản ghi nhóm khách hàng") @PathVariable Long id) {
        return ResponseUtils.success(customerGroupService.getCustomerGroup(id));
    }

    @Operation(
            summary =
                    "Api cập nhật bản ghi nhóm khách hàng theo id (tương đương với việc tạo mới bản ghi chờ duyệt từ 1 bản ghi đã có)")
    @PreAuthorize(Authority.CustomerGroup.UPDATE_CUSTOMER_GROUP)
    @PutMapping("/customer-groups/{id}")
    public ResponseEntity<ResponseData<Void>> updateCustomerGroup(
            @Parameter(description = "ID bản ghi nhóm khách hàng") @PathVariable Long id,
            @RequestBody CustomerGroupInput customerGroupInput) {
        customerGroupService.updateCustomerGroup(id, customerGroupInput);
        return ResponseUtils.success();
    }

    @Operation(summary = "Api phê duyệt nhóm khách hàng")
    @PreAuthorize(Authority.CustomerGroup.APPROVE_CUSTOMER_GROUP)
    @PutMapping("/customer-groups/approvals")
    public ResponseEntity<ResponseData<Void>> approveCustomerGroup(
            @Valid @RequestBody ApprovalInput input) {
        customerGroupService.approveCustomerGroup(input);
        return ResponseUtils.success();
    }

    @Operation(summary = "Api (nội bộ) kiểm tra nhóm khách hàng theo id")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @GetMapping("/internal/customer-groups/existed")
    public ResponseEntity<ResponseData<Boolean>> checkCustomerGroupExisted(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey,
            @RequestParam Long id) {
        return ResponseUtils.success(customerGroupService.checkCustomerGroupExisted(id));
    }

    @Operation(summary = "Api (nội bộ) lấy danh sách nhóm khách hàng mà khách hàng thuộc về")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @GetMapping("/internal/customer-groups")
    public ResponseEntity<ResponseData<List<InternalCustomerGroupOutput>>> getInternalCustomerGroup(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey,
            @Parameter(description = "Mã định danh của khách hàng trên bank")
            @RequestParam(required = false)
            String cifBank,
            @Parameter(description = "Mã định danh của khách hàng trên ví")
            @RequestParam(required = false)
            String cifWallet,
            @Parameter(description = "ID của khách hàng") @RequestParam(required = false)
            Long customerId) {
        return ResponseUtils.success(
                customerGroupService.getInternalCustomerGroup(customerId, cifBank, cifWallet));
    }

    @Operation(summary = "Api update notification lv24h")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @PostMapping("/internal/update/notification")
    public ResponseEntity<ResponseData<Void>> updateNotification(
            @Valid @RequestBody CustomerGroupInput customerGroupInput) {
        customerGroupService.createCustomerGroup(customerGroupInput);
        return ResponseUtils.success();
    }
}
