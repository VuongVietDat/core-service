package vn.com.atomi.loyalty.base.redis;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.base.security.UserOutput;

@Repository
@RequiredArgsConstructor
public class CacheUserRepository {

  private final RedisTemplate<String, Object> redisTemplate;

  private String composeHeader(String key) {
    return String.format("LOYALTY_CACHE_USER:%s", key);
  }

  public Optional<UserOutput> get(String key) {
    return Optional.ofNullable((UserOutput) redisTemplate.opsForValue().get(composeHeader(key)));
  }
}
