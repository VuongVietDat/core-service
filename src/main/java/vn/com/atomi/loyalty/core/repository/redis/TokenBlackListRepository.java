package vn.com.atomi.loyalty.core.repository.redis;

import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.core.entity.redis.TokenBlackList;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
@RequiredArgsConstructor
public class TokenBlackListRepository {

  private final RedisTemplate<String, Object> redisTemplate;

  public Optional<TokenBlackList> find(String key) {
    return Optional.ofNullable(
        (TokenBlackList) this.redisTemplate.opsForValue().get(composeHeader(key)));
  }

  public void put(TokenBlackList tokenBlackList) {
    this.redisTemplate
        .opsForValue()
        .set(
            composeHeader(tokenBlackList.getSessionId()),
            tokenBlackList,
            Duration.ofSeconds(tokenBlackList.getExpirationSeconds()));
  }

  private String composeHeader(String key) {
    return String.format("TokenBlackList:%s", key);
  }
}
