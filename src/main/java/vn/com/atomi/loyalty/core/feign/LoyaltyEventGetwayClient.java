package vn.com.atomi.loyalty.core.feign;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.core.dto.input.NotificationInput;
import vn.com.atomi.loyalty.core.dto.output.EGCBiometricOutput;
import vn.com.atomi.loyalty.core.dto.output.NotificationOutput;
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

    @Operation(summary = "Api (noi bo) lấy danh sách khách hàng hoàn thiện sinh trắc học")
    @GetMapping("/internal/completebiometric")
    ResponseData<List<EGCBiometricOutput>> getLstCompleteBiometric(@RequestHeader(RequestConstant.REQUEST_ID) String requestId);


    @Operation(
            summary = "Api (nội bộ) tự động chuyển trạng thái đã cộng điểm hoàn thành sinh trắc học cho khách hàng")
    @PutMapping("/internal/completebiometric/update")
    ResponseData<String> automaticupdate(
            @RequestHeader(RequestConstant.REQUEST_ID) String requestId,
            @RequestParam String cifBank);
}
