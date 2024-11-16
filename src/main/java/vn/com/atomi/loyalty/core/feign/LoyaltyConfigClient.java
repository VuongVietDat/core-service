package vn.com.atomi.loyalty.core.feign;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.core.dto.output.CustomerCasa;
import vn.com.atomi.loyalty.core.dto.output.RankOutput;
import vn.com.atomi.loyalty.core.dto.output.RuleOutput;
import vn.com.atomi.loyalty.core.dto.output.RulePOCOutput;
import vn.com.atomi.loyalty.core.feign.fallback.LoyaltyConfigClientFallbackFactory;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@FeignClient(
        name = "loyalty-config-service",
        url = "${custom.properties.loyalty-config-service-url}",
        fallbackFactory = LoyaltyConfigClientFallbackFactory.class)
public interface LoyaltyConfigClient {

    @GetMapping("/internal/campaigns/active-by-group")
    ResponseData<Boolean> checkCampaignActive(
            @RequestHeader(RequestConstant.REQUEST_ID) String requestId,
            @RequestParam Long customerGroupId);

    @Operation(summary = "Api (nội bộ) lấy tất cả hạng")
    @GetMapping("/internal/ranks")
    ResponseData<List<RankOutput>> getAllRanks(
            @RequestHeader(RequestConstant.REQUEST_ID) String requestId);

    @Operation(
            summary =
                    "Api (nội bộ) lấy tất cả quy tắc theo loại quy tắc và có hiệu lực tại thời điểm hiện tại")
    @GetMapping("/internal/rules")
    ResponseData<List<RuleOutput>> getAllActiveRule(
            @RequestHeader(RequestConstant.REQUEST_ID) String requestId,
            @RequestParam String type,
            @RequestParam(required = false) String transactionAt);

    @Operation(summary = "Api (nội bộ) lấy tất cả quy tắc theo kịch bản phục vụ POC")
    @GetMapping("/internal/rules/poc")
    ResponseData<RulePOCOutput> getRulePoc(
            @RequestHeader(RequestConstant.REQUEST_ID) String requestId,
            @RequestParam String type);

}
