package vn.com.atomi.loyalty.base.security;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.ResponseData;

/**
 * @author haidv
 * @version 1.0
 */
@FeignClient(
    name = "userinfo-service",
    url = "${custom.properties.loyalty-common-service-url}",
    fallbackFactory = UserinfoClientFallbackFactory.class)
public interface UserinfoClient {

  @Operation(summary = "Api (nội bộ) lấy thông tin người dùng theo username")
  @GetMapping("/internal/auth/user")
  ResponseData<UserOutput> getUser(
      @RequestHeader(RequestConstant.REQUEST_ID) String requestId, @RequestParam String username);
}
