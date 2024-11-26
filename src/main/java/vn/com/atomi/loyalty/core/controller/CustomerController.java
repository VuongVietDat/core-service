package vn.com.atomi.loyalty.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.annotations.DateTimeValidator;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.BaseController;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.data.ResponseUtils;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.core.dto.output.CustomerOutput;
import vn.com.atomi.loyalty.core.dto.output.CustomerPointAccountOutput;
import vn.com.atomi.loyalty.core.dto.output.CustomerPointAccountPreviewOutput;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.service.CustomerBalanceService;
import vn.com.atomi.loyalty.core.service.CustomerService;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class CustomerController extends BaseController {

    private final CustomerService customerService;

    private final CustomerBalanceService customerBalanceService;

    @Operation(summary = "Api lấy chi tiết tài khoản điểm theo id khách hàng")
    @PreAuthorize(Authority.Customer.READ_CUSTOMER_ACCOUNT)
    @GetMapping("/customers/{id}/point-accounts")
    public ResponseEntity<ResponseData<CustomerPointAccountOutput>> getCustomerPointAccount(
            @Parameter(description = "ID khách hàng bên loyalty") @PathVariable Long id) {
        return ResponseUtils.success(customerService.getCustomerPointAccount(id));
    }

    @Operation(summary = "Api lấy danh sách tài khoản điểm")
    @PreAuthorize(Authority.Customer.READ_CUSTOMER_ACCOUNT)
    @GetMapping("/customers/point-accounts")
    public ResponseEntity<ResponseData<ResponsePage<CustomerPointAccountPreviewOutput>>>
    getCustomerPointAccounts(
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
            @Parameter(description = "ID khách hàng bên loyalty") @RequestParam(required = false)
            Long customerId,
            @Parameter(description = "Tên khách hàng bên loyalty") @RequestParam(required = false)
            String customerName,
            @Parameter(description = "Mã định danh của khách hàng trên bank")
            @RequestParam(required = false)
            String cifBank,
            @Parameter(description = "Mã định danh của khách hàng trên ví")
            @RequestParam(required = false)
            String cifWallet,
            @Parameter(description = "Số giấy tờ tùy thân") @RequestParam(required = false)
            String uniqueValue,
            @Parameter(description = "Số điểm lớn hơn hoặc bằng") @RequestParam(required = false)
            Long pointFrom,
            @Parameter(description = "Số điểm nhỏ hơn hoặc bằng") @RequestParam(required = false)
            Long pointTo) {
        return ResponseUtils.success(
                customerService.getCustomerPointAccounts(
                        status,
                        customerId,
                        customerName,
                        cifBank,
                        cifWallet,
                        uniqueValue,
                        pointFrom,
                        pointTo,
                        super.pageable(pageNo, pageSize, sort)));
    }

    @GetMapping("/customers")
    @Operation(summary = "Api lấy danh sách thành viên")
    @PreAuthorize(Authority.Customer.READ_CUSTOMER_ACCOUNT)
    public ResponseEntity<ResponseData<ResponsePage<CustomerOutput>>> gets(
            @Parameter(description = "Số trang, bắt đầu từ 1", example = "1") @RequestParam
            Integer pageNo,
            @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200", example = "10") @RequestParam
            Integer pageSize,
            @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
            @RequestParam(required = false)
            String sort,
            @Parameter(description = "ID khách hàng bên loyalty") @RequestParam(required = false)
            Long customerId,
            @Parameter(description = "Tên khách hàng bên loyalty") @RequestParam(required = false)
            String customerName,
            @Parameter(description = "Mã định danh của khách hàng trên bank")
            @RequestParam(required = false)
            String cifBank,
            @Parameter(description = "Xếp hạng KH") @RequestParam(required = false) String rank,
            @Parameter(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
            @RequestParam(required = false)
            Status status,
            @Parameter(description = "Phân khúc KH: LUXURY, ENTRY", example = "ENTRY")
            @RequestParam(required = false)
            String segment) {

        return ResponseUtils.success(
                customerService.gets(
                        status,
                        customerId,
                        customerName,
                        cifBank,
                        rank,
                        segment,
                        super.pageable(pageNo, pageSize, sort)));
    }

    @Operation(summary = "Api lấy chi tiết thành viên theo id")
    @PreAuthorize(Authority.Customer.READ_CUSTOMER_ACCOUNT)
    @GetMapping("/customers/{id}")
    public ResponseEntity<ResponseData<CustomerOutput>> getDetail(
            @Parameter(description = "ID khách hàng bên loyalty") @PathVariable Long id) {
        return ResponseUtils.success(customerService.get(id));
    }

    @Operation(summary = "Api (nội bộ) lấy chi tiết thành viên theo mã ví")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @GetMapping("/internal/customers")
    public ResponseEntity<ResponseData<CustomerOutput>> getCustomer(
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
            String cifWallet) {
        return ResponseUtils.success(customerService.getCustomer(cifBank, cifWallet));
    }
}
