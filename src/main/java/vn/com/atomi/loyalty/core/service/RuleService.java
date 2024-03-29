package vn.com.atomi.loyalty.core.service;

import java.util.List;
import vn.com.atomi.loyalty.core.dto.output.RuleOutput;

/**
 * @author haidv
 * @version 1.0
 */
public interface RuleService {

  List<RuleOutput> getAllActiveRule(String type, String transactionAt);
}
