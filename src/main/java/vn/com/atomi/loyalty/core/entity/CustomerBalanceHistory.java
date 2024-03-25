package vn.com.atomi.loyalty.core.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.core.enums.ChangeType;
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
@Entity
@Table(name = "C_CUSTOMER_BALANCE_HISTORY")
public class CustomerBalanceHistory extends BaseEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "C_BALANCE_HISTORY_ID_SEQ")
  @SequenceGenerator(
      name = "C_BALANCE_HISTORY_ID_SEQ",
      sequenceName = "C_BALANCE_HISTORY_ID_SEQ",
      allocationSize = 1)
  private Long id;

  @Column(name = "TRANSACTION_ID")
  private Long transactionId;

  @Column(name = "CUSTOMER_ID")
  private Long customerId;

  @Column(name = "CHANGE_TYPE")
  @Enumerated(EnumType.STRING)
  private ChangeType changeType;

  @Column(name = "POINT_TYPE")
  @Enumerated(EnumType.STRING)
  private PointType pointType;

  @Column(name = "AMOUNT")
  private Long amount;

  @Column(name = "AMOUNT_USED")
  private Long amountUsed;

  @Column(name = "AMOUNT_EXPIRED")
  private Long amountExpired;

  @Column(name = "BEFORE_AMOUNT")
  private Long beforeAmount;

  @Column(name = "AFTER_AMOUNT")
  private Long afterAmount;

  @Column(name = "REF_NO")
  private String refNo;

  @Column(name = "CONTENT")
  private String content;

  @Column(name = "CAMPAIGN_ID")
  private Long campaignId;

  @Column(name = "CAMPAIGN_CODE")
  private String campaignCode;

  @Column(name = "RULE_ID")
  private Long ruleId;

  @Column(name = "RULE_CODE")
  private String ruleCode;

  @Column(name = "TRANSACTION_AT")
  private LocalDateTime transactionAt;

  @Column(name = "EXPIRE_AT")
  private LocalDate expireAt;

  @Column(name = "SEARCH_TRANSACTION_DATE")
  private String searchTransactionDate;

  @Column(name = "TRANSACTION_USED")
  private String transactionUsed;
}
