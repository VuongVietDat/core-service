package vn.com.atomi.loyalty.base.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.base.exception.CommonErrorCode;

/**
 * @author haidv
 * @version 1.0
 */
@Component
@Slf4j
public class UserinfoClientFallbackFactory implements FallbackFactory<UserinfoClient> {
  @Override
  public UserinfoClient create(Throwable cause) {
    log.error("An exception occurred when calling the LoyaltyCommonClient", cause);

    return new UserinfoClient() {
      @Override
      public ResponseData<UserOutput> getUser(String requestId, String username) {
        throw new BaseException(CommonErrorCode.UNAUTHORIZED);
      }
    };
  }
}
