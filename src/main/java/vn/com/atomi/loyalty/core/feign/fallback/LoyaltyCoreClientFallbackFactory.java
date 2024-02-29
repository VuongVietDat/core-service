package vn.com.atomi.loyalty.core.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.core.feign.LoyaltyCoreClient;

/**
 * @author haidv
 * @version 1.0
 */
@Component
@Slf4j
public class LoyaltyCoreClientFallbackFactory implements FallbackFactory<LoyaltyCoreClient> {
  @Override
  public LoyaltyCoreClient create(Throwable cause) {
    log.error("An exception occurred when calling the AuthClient", cause);
    return new LoyaltyCoreClient() {
      @Override
      public ResponseData<Void> example(String requestId) {
        log.info("[Fallback] example");
        return null;
      }
    };
  }
}
