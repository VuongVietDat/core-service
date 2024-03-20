package vn.com.atomi.loyalty.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.data.BaseController;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.data.ResponseUtils;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.core.dto.output.CustomerPointAccountOutput;
import vn.com.atomi.loyalty.core.dto.output.CustomerPointAccountPreviewOutput;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.service.CustomerService;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class CustomerController extends BaseController {

  private final CustomerService customerService;

  @Operation(summary = "Api lấy chi tiết tài khoản điểm theo id khách hàng")
  @PreAuthorize(Authority.CustomerAccount.READ_CUSTOMER_ACCOUNT)
  @GetMapping("/customers/{id}/point-accounts")
  public ResponseEntity<ResponseData<CustomerPointAccountOutput>> getCustomerPointAccount(
      @Parameter(description = "ID khách hàng bên loyalty") @PathVariable Long id) {
    return ResponseUtils.success(customerService.getCustomerPointAccount(id));
  }

  @Operation(summary = "Api lấy danh sách tài khoản điểm")
  @PreAuthorize(Authority.CustomerAccount.READ_CUSTOMER_ACCOUNT)
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
          @Parameter(description = "Xếp hạng KH") @RequestParam(required = false) String rank,
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
            rank,
            super.pageable(pageNo, pageSize, sort)));
  }
}
