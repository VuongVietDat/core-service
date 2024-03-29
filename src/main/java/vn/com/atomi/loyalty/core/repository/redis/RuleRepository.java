package vn.com.atomi.loyalty.core.repository.redis;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.base.utils.JsonUtils;
import vn.com.atomi.loyalty.core.dto.output.RuleOutput;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
@RequiredArgsConstructor
public class RuleRepository {

  private static final String KEY_RULE_CURRENT_ACTIVE = "LOYALTY_RULE_CURRENT_ACTIVE";
  private final RedisTemplate<String, Object> redisTemplate;

  public List<RuleOutput> getRuleCurrentActive(String type) {
    var opt =
        (String)
            this.redisTemplate
                .opsForValue()
                .get(String.format("%s:%s", KEY_RULE_CURRENT_ACTIVE, type));
    return opt == null ? new ArrayList<>() : JsonUtils.fromJson(opt, List.class, RuleOutput.class);
  }

  public void putRuleCurrentActive(String type, List<RuleOutput> ruleOutputs) {
    redisTemplate
        .opsForValue()
        .set(
            String.format("%s:%s", KEY_RULE_CURRENT_ACTIVE, type),
            JsonUtils.toJson(ruleOutputs),
            Duration.ofHours(4));
  }
}
