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
import vn.com.atomi.loyalty.core.dto.input.TransactionInput;
import vn.com.atomi.loyalty.core.dto.output.CustomerBalanceOutput;
import vn.com.atomi.loyalty.core.dto.output.ExternalCustomerBalanceHistoryOutput;
import vn.com.atomi.loyalty.core.enums.ChangeType;
import vn.com.atomi.loyalty.core.service.CustomerBalanceService;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class CustomerBalanceController extends BaseController {

  private final CustomerBalanceService customerBalanceService;

  @Operation(summary = "Api (nội bộ) tiêu điểm")
  @PreAuthorize(Authority.ROLE_SYSTEM)
  @PostMapping("/internal/transactions-minus")
  public ResponseEntity<ResponseData<Void>> executeTransactionMinus(
      @Parameter(
              description = "Chuỗi xác thực khi gọi api nội bộ",
              example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
          @RequestHeader(RequestConstant.SECURE_API_KEY)
          @SuppressWarnings("unused")
          String apiKey,
      @RequestBody TransactionInput transactionInput) {
    transactionInput.setChangeType(ChangeType.MINUS);
    customerBalanceService.executeTransactionMinus(transactionInput);
    return ResponseUtils.success();
  }

  @Operation(summary = "Api (nội bộ) lấy thông tin số dư hiện tại của khách hàng")
  @PreAuthorize(Authority.ROLE_SYSTEM)
  @GetMapping("/internal/customers/balances")
  public ResponseEntity<ResponseData<CustomerBalanceOutput>> getCurrentBalance(
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
    return ResponseUtils.success(customerBalanceService.getCurrentBalance(cifBank, cifWallet));
  }

  @Operation(summary = "Api (nội bộ) lấy lịch sử biến động số dư điểm của khách hàng")
  @PreAuthorize(Authority.ROLE_SYSTEM)
  @GetMapping("/internal/customers/balances-histories")
  public ResponseEntity<ResponseData<ResponsePage<ExternalCustomerBalanceHistoryOutput>>>
      getBalanceHistories(
          @Parameter(
                  description = "Chuỗi xác thực khi gọi api nội bộ",
                  example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
              @RequestHeader(RequestConstant.SECURE_API_KEY)
              @SuppressWarnings("unused")
              String apiKey,
          @Parameter(description = "Số trang, bắt đầu từ 1", example = "1") @RequestParam
              Integer pageNo,
          @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200", example = "10")
              @RequestParam
              Integer pageSize,
          @Parameter(
                  description =
                      "ID khách hàng bên loyalty (lấy từ field customerId ở api lấy số dư hiện tại)")
              @RequestParam
              Long customerId,
          @Parameter(description = "Loại thay đổi: </br>PLUS: Cộng điểm</br>PLUS: Trừ điểm")
              @RequestParam(required = false)
              ChangeType changeType,
          @Parameter(
                  description = "Thời gian giao dịch từ ngày (yyyy/MM/dd)",
                  example = "2024/01/01")
              @DateTimeValidator(
                  required = false,
                  pattern = DateConstant.ISO_8601_EXTENDED_DATE_FORMAT_STROKE)
              @RequestParam(required = false)
              String startDate,
          @Parameter(
                  description = "Thời gian giao dịch đến ngày (yyyy/MM/dd)",
                  example = "2024/12/31")
              @DateTimeValidator(
                  required = false,
                  pattern = DateConstant.ISO_8601_EXTENDED_DATE_FORMAT_STROKE)
              @RequestParam(required = false)
              String endDate) {
    return ResponseUtils.success(
        customerBalanceService.getBalanceHistories(
            customerId, changeType, startDate, endDate, super.pageable(pageNo, pageSize, null)));
  }
}
