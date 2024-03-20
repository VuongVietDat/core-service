package vn.com.atomi.loyalty.core.feign;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.core.dto.output.RankOutput;
import vn.com.atomi.loyalty.core.feign.fallback.LoyaltyConfigClientFallbackFactory;

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
}
