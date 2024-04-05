package vn.com.atomi.loyalty.core.dto.input;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import vn.com.atomi.loyalty.core.enums.PointEventSource;
import vn.com.atomi.loyalty.core.enums.PointType;

/**
 * @author haidv
 * @version 1.0
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionInput {

  private Long customerId;

  private String refNo;

  private Long amount;

  private Long transactionAmount;

  private LocalDateTime transactionAt;

  private PointType pointType;

  private String productType;

  private String productLine;

  private String currency;

  private String chanel;

  private String transactionGroup;

  private String transactionType;

  private Long ruleId;

  private String ruleCode;

  private Long campaignId;

  private String campaignCode;

  private String content;

  private LocalDate expireAt;

  private PointEventSource eventSource;

  private Long budgetId;

  private String budgetCode;
}
