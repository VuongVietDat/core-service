package vn.com.atomi.loyalty.core.feign;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.core.dto.input.NotificationInput;
import vn.com.atomi.loyalty.core.dto.output.NotificationOutput;
import vn.com.atomi.loyalty.core.dto.output.RuleOutput;
import vn.com.atomi.loyalty.core.feign.fallback.LoyaltyEventGetwayFallbackFactory;

import java.util.List;


@FeignClient(
        name = "loyalty-eventgetway-service",
        url = "${custom.properties.loyalty-eventgetway-service-url}",
        fallbackFactory = LoyaltyEventGetwayFallbackFactory.class)
public interface LoyaltyEventGetwayClient {

    @Operation(
            summary =
                    "Api gửi thông báo tới khách hàng")
    @GetMapping("/internal/notification")
    ResponseData<List<NotificationOutput>> sendNotification(
            @RequestHeader(RequestConstant.REQUEST_ID) String requestId,
            @Valid @RequestBody NotificationInput request);
}
