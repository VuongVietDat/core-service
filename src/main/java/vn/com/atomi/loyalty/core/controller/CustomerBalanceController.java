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
import vn.com.atomi.loyalty.core.dto.input.UsePointInput;
import vn.com.atomi.loyalty.core.dto.output.CustomerBalanceHistoryOutput;
import vn.com.atomi.loyalty.core.dto.output.CustomerBalanceOutput;
import vn.com.atomi.loyalty.core.dto.output.ExternalCustomerBalanceHistoryOutput;
import vn.com.atomi.loyalty.core.enums.ChangeType;
import vn.com.atomi.loyalty.core.enums.PointType;
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
            @RequestBody UsePointInput usePointInput) {
        usePointInput.setChangeType(ChangeType.MINUS_CONSUMPTION);
        customerBalanceService.executeTransactionMinus(usePointInput);
        return ResponseUtils.success();
    }

    @Operation(summary = "Api (nội bộ) thực hiện hết hạn điểm")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @PostMapping("/internal/points-expiration")
    public ResponseEntity<ResponseData<Void>> executePointExpiration(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey) {
        customerBalanceService.executePointExpiration();
        return ResponseUtils.success();
    }


    @Operation(summary = "Api (nội bộ) lấy thông tin số dư hiện tại của tôi")
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

    @Operation(summary = "Api (nội bộ) lấy lịch sử biến động số dư điểm của tôi")
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
            @Parameter(
                    description =
                            "Loại thay đổi: </br>PLUS: Cộng điểm</br>MINUS_CONSUMPTION: Trừ điểm tiêu dùng</br>MINUS_EXPIRED: Trừ điểm hết hạn")
            @RequestParam(required = false)
            ChangeType changeType,
            @Parameter(
                    description = "Thời gian giao dịch từ ngày (dd/MM/yyyy)",
                    example = "01/01/2024")
            @DateTimeValidator(
                    required = false,
                    pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
            @RequestParam(required = false)
            String startTransactionDate,
            @Parameter(
                    description = "Thời gian giao dịch đến ngày (dd/MM/yyyy)",
                    example = "31/12/2024")
            @DateTimeValidator(
                    required = false,
                    pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
            @RequestParam(required = false)
            String endTransactionDate) {
        return ResponseUtils.success(
                customerBalanceService.getBalanceHistories(
                        customerId,
                        changeType,
                        startTransactionDate,
                        endTransactionDate,
                        super.pageable(pageNo, pageSize, null)));
    }

    @Operation(summary = "Api lấy lịch sử biến động số dư điểm của khách hàng")
    @PreAuthorize(Authority.Customer.READ_BALANCE_HISTORY)
    @GetMapping("/customers/balances-histories")
    public ResponseEntity<ResponseData<ResponsePage<CustomerBalanceHistoryOutput>>>
    getBalanceHistories(
            @Parameter(description = "Số trang, bắt đầu từ 1", example = "1") @RequestParam
            Integer pageNo,
            @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200", example = "10")
            @RequestParam
            Integer pageSize,
            @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
            @RequestParam(required = false)
            String sort,
            @Parameter(
                    description =
                            "ID khách hàng bên loyalty (lấy từ field customerId ở api lấy số dư hiện tại)")
            @RequestParam(required = false)
            Long customerId,
            @Parameter(
                    description =
                            "Loại thay đổi: </br>PLUS: Cộng điểm</br>MINUS_CONSUMPTION: Trừ điểm tiêu dùng</br>MINUS_EXPIRED: Trừ điểm hết hạn")
            @RequestParam(required = false)
            ChangeType changeType,
            @Parameter(
                    description =
                            "Loại điểm: </br>RANK_POINT: Điểm xếp hạng</br>CONSUMPTION_POINT: Điểm tiêu dùng")
            @RequestParam(required = false)
            PointType pointType,
            @Parameter(
                    description = "Thời gian giao dịch từ ngày (dd/MM/yyyy)",
                    example = "01/01/2024")
            @DateTimeValidator(
                    required = false,
                    pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
            @RequestParam(required = false)
            String startTransactionDate,
            @Parameter(
                    description = "Thời gian giao dịch đến ngày (dd/MM/yyyy)",
                    example = "31/12/2024")
            @DateTimeValidator(
                    required = false,
                    pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
            @RequestParam(required = false)
            String endTransactionDate,
            @Parameter(description = "Thời gian hết hạn từ ngày (dd/MM/yyyy)", example = "01/01/2024")
            @DateTimeValidator(
                    required = false,
                    pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
            @RequestParam(required = false)
            String startExpiredDate,
            @Parameter(
                    description = "Thời gian hết hạn đến ngày (dd/MM/yyyy)",
                    example = "31/12/2024")
            @DateTimeValidator(
                    required = false,
                    pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
            @RequestParam(required = false)
            String endExpiredDate) {
        return ResponseUtils.success(
                customerBalanceService.getBalanceHistories(
                        customerId,
                        changeType,
                        pointType,
                        startTransactionDate,
                        endTransactionDate,
                        startExpiredDate,
                        endExpiredDate,
                        super.pageable(pageNo, pageSize, sort)));
    }

    @Operation(summary = "Api (public) lấy thông tin số dư điểm hiện tại ")
//    @PreAuthorize(Authority.ROLE_SYSTEM)
    @GetMapping("/customers/balances")
    public ResponseEntity<ResponseData<CustomerBalanceOutput>> getPublicCurrentBalance(
            @Parameter(description = "Mã định danh của khách hàng trên bank")
            @RequestParam(required = false)
            String cifBank,
            @Parameter(description = "Mã định danh của khách hàng trên ví")
            @RequestParam(required = false)
            String cifWallet) {
        return ResponseUtils.success(customerBalanceService.getCurrentBalance(cifBank, cifWallet));
    }

    @Operation(summary = "Api (nội bộ) thực hiện tính điểm dựa vào số dư CASA bình quân cua KH")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @PostMapping("/internal/customers/points/casa")
    public ResponseEntity<ResponseData<Void>> calculatePointCasa(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey) {
        customerBalanceService.calculatePointCasa();
        return ResponseUtils.success();
    }


    @Operation(summary = "Api (nội bộ) thực hiện tính điểm cho giao dịch mua bán ngoại tệ tại quầy cua KH")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @PostMapping("/internal/customers/points/currencyTransaction")
    public ResponseEntity<ResponseData<Void>> calculatePointCurrencyTransaction(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey,
            @Parameter(
                    description = "Thời gian lay giao dich ngoai te từ ngày (dd/MM/yyyy)",
                    example = "01/01/2024")
            @DateTimeValidator(
                    required = false,
                    pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
            @RequestParam(required = false)
            String startDate,
            @Parameter(
                    description = "Thời gian lay giao dich ngoai te đến ngày (dd/MM/yyyy)",
                    example = "31/12/2024")
            @DateTimeValidator(
                    required = false,
                    pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
            @RequestParam(required = false)
            String endDate) {
        customerBalanceService.calculatePointCurrencyTransaction(startDate, endDate);
        return ResponseUtils.success();
    }

    @Operation(summary = "Api (nội bộ) thực hiện tính điểm cho giao dịch thẻ")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @PostMapping("/internal/customers/points/card")
    public ResponseEntity<ResponseData<Void>> calculatePointCard(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey,
            @Parameter(
                    description = "Thời gian lay giao dich ngoai te từ ngày (dd/MM/yyyy)",
                    example = "01/01/2024")
            @DateTimeValidator(
                    required = false,
                    pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
            @RequestParam(required = false)
            String startDate,
            @Parameter(
                    description = "Thời gian lay giao dich ngoai te đến ngày (dd/MM/yyyy)",
                    example = "31/12/2024")
            @DateTimeValidator(
                    required = false,
                    pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
            @RequestParam(required = false)
            String endDate) {
        customerBalanceService.calculatePointCard(startDate, endDate);
        return ResponseUtils.success();
    }

    @Operation(summary = "Api (nội bộ) thực hiện tính điểm khách hàng hoàn thiện sinh trắc học")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @PostMapping("/internal/customers/points/completebiometric")
    public ResponseEntity<ResponseData<Void>> calculateCompleteBiometric(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey) {
        customerBalanceService.calculateCompleteBiometric();
        return ResponseUtils.success();
    }

}

