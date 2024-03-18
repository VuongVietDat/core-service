package vn.com.atomi.loyalty.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.data.ResponseUtils;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.core.dto.output.CustomerBalanceOutput;
import vn.com.atomi.loyalty.core.service.CustomerBalanceService;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class CustomerBalanceController {

  private final CustomerBalanceService customerBalanceService;

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
      @RequestParam(required = false) String cifBank,
      @RequestParam(required = false) String cifWallet) {
    return ResponseUtils.success(customerBalanceService.getCurrentBalance(cifBank, cifWallet));
  }
}
