package vn.com.atomi.loyalty.core.feign;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.com.atomi.loyalty.base.annotations.DateTimeValidator;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.data.ResponseUtils;
import vn.com.atomi.loyalty.core.feign.fallback.LoyaltyGiftClientFallbackFactory;
import vn.com.atomi.loyalty.gift.dto.output.GiftPartnerOutput;
import vn.com.atomi.loyalty.gift.enums.Status;

import java.util.List;

@FeignClient(
        name = "loyalty-gift-service",
        url = "${custom.properties.loyalty-gift-service-url}",
        fallbackFactory = LoyaltyGiftClientFallbackFactory.class)
public interface LoyaltyGiftClient {
    @Operation(summary = "Api lấy danh sách quà ")
    @GetMapping("/gift_partners")
    public ResponseData<List<GiftPartnerOutput>> getGiftPartners(
            @Parameter(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
            @RequestParam(required = false)
            Status status,
            @Parameter(description = "Thời gian hiệu lực từ ngày (dd/MM/yyyy)", example = "01/01/2024")
            @DateTimeValidator(required = false, pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
            @RequestParam(required = false)
            String effectiveDate,
            @Parameter(description = "Tên quà")
            @RequestParam(required = false)
            String name,
            @Parameter(description = "Id đối tác")
            @RequestParam(required = false)
            Long partnerId,
            @Parameter(description = "Id danh mục")
            @RequestParam(required = false)
            Long categoryId,
            @Parameter(description = "Mã danh mục") @RequestParam(required = false) String categoryCode);

}
