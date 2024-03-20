package vn.com.atomi.loyalty.core.feign.fallback;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.base.exception.CommonErrorCode;
import vn.com.atomi.loyalty.core.dto.output.RankOutput;
import vn.com.atomi.loyalty.core.feign.LoyaltyConfigClient;

/**
 * @author haidv
 * @version 1.0
 */
@Component
@Slf4j
public class LoyaltyConfigClientFallbackFactory implements FallbackFactory<LoyaltyConfigClient> {
  @Override
  public LoyaltyConfigClient create(Throwable cause) {
    log.error("An exception occurred when calling the AuthClient", cause);
    return new LoyaltyConfigClient() {
      @Override
      public ResponseData<Boolean> checkCampaignActive(String requestId, Long customerGroupId) {
        throw new BaseException(CommonErrorCode.EXECUTE_THIRTY_SERVICE_ERROR, cause);
      }

      @Override
      public ResponseData<List<RankOutput>> getAllRanks(String requestId) {
        log.info("getAllRanks: set default empty array");
        return new ResponseData<List<RankOutput>>().success(new ArrayList<>());
      }
    };
  }
}
