package vn.com.atomi.loyalty.core.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.*;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.core.enums.ChangeType;

/**
 * @author haidv
 * @version 1.0
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExternalCustomerBalanceHistoryOutput {

  private ChangeType changeType;

  private Long amount;

  private Long beforeAmount;

  private Long afterAmount;

  private String refNo;

  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS_STROKE)
  private LocalDateTime transactionAt;
}
