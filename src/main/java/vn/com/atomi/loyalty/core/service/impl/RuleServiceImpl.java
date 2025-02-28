package vn.com.atomi.loyalty.core.service.impl;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.utils.RequestUtils;
import vn.com.atomi.loyalty.core.dto.output.RuleOutput;
import vn.com.atomi.loyalty.core.feign.LoyaltyConfigClient;
import vn.com.atomi.loyalty.core.repository.redis.RuleRepository;
import vn.com.atomi.loyalty.core.service.RuleService;

/**
 * @author haidv
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class RuleServiceImpl extends BaseService implements RuleService {

  private final LoyaltyConfigClient loyaltyConfigClient;

  private final RuleRepository ruleRepository;

  @Override
  public List<RuleOutput> getAllActiveRule(String type, String transactionAt) {
    var ruleOutputs = ruleRepository.getRuleCurrentActive(type);
    if (CollectionUtils.isEmpty(ruleOutputs)) {
      return renewCacheActiveRule(type, transactionAt);
    }
    for (RuleOutput ruleOutput : ruleOutputs) {
      if (ruleOutput.getEndDate() != null && LocalDate.now().isAfter(ruleOutput.getEndDate())) {
        return renewCacheActiveRule(type, transactionAt);
      }
    }
    return ruleOutputs;
  }

  private List<RuleOutput> renewCacheActiveRule(String type, String transactionAt) {
    var ruleOutputs =
        loyaltyConfigClient
            .getAllActiveRule(RequestUtils.extractRequestId(), type, transactionAt)
            .getData();
    if (!CollectionUtils.isEmpty(ruleOutputs)) {
      ruleRepository.putRuleCurrentActive(type, ruleOutputs);
    }
    return ruleOutputs;
  }
}
