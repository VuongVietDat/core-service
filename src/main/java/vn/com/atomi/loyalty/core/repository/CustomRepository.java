package vn.com.atomi.loyalty.core.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import vn.com.atomi.loyalty.core.enums.ChangeType;
import vn.com.atomi.loyalty.core.enums.PointType;

/**
 * @author haidv
 * @version 1.0
 */
public interface CustomRepository {

  Long plusAmount(
      Long customerId,
      Long amount,
      String refNo,
      String campaignCode,
      Long campaignId,
      String ruleCode,
      Long ruleId,
      LocalDateTime transactionAt,
      String content,
      LocalDate expireAt,
      PointType pointType,
      ChangeType changeType);
}
