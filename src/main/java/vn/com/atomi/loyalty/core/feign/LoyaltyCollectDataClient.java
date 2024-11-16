package vn.com.atomi.loyalty.core.feign;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.core.dto.output.CurrencyTransaction;
import vn.com.atomi.loyalty.core.dto.output.CustomerCasa;
import vn.com.atomi.loyalty.core.feign.fallback.LoyaltyCollectDataClientFallbackFactory;

import java.util.List;

@FeignClient(
        name = "loyalty-collectdata-service",
        url = "${custom.properties.loyalty-collectdata-service-url}",
        fallbackFactory = LoyaltyCollectDataClientFallbackFactory.class)
public interface LoyaltyCollectDataClient {

    @Operation(summary = "Api (nội bộ) lấy danh sach số dư casa hiện tại cua khách hàng")
    @GetMapping("/internal/customers/casa")
    ResponseData<List<CustomerCasa>> getLstCurrentCasa(@RequestHeader(RequestConstant.REQUEST_ID) String requestId);

    @Operation(summary = "Api (nội bộ) lấy thông tin giao dich ngoai te")
    @GetMapping("/internal/customers/currency")
    ResponseData<List<CurrencyTransaction>> getCustomerCurrencyTransactions(@RequestHeader(RequestConstant.REQUEST_ID) String requestId,
                                                                            @RequestParam(required = false) String startDate,
                                                                            @RequestParam(required = false) String endDate);
}
