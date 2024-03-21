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
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "c_balance_history_id_seq")
  @SequenceGenerator(
      name = "c_balance_history_id_seq",
      sequenceName = "c_balance_history_id_seq",
      allocationSize = 1)
  private Long id;

  @Column(name = "transaction_id")
  private Long transactionId;

  @Column(name = "customer_id")
  private Long customerId;

  @Column(name = "change_type")
  @Enumerated(EnumType.STRING)
  private ChangeType changeType;

  @Column(name = "point_type")
  @Enumerated(EnumType.STRING)
  private PointType pointType;

  @Column(name = "amount")
  private Long amount;

  @Column(name = "amount_used")
  private Long amountUsed;

  @Column(name = "before_amount")
  private Long beforeAmount;

  @Column(name = "after_amount")
  private Long afterAmount;

  @Column(name = "ref_no")
  private String refNo;

  @Column(name = "content")
  private String content;

  @Column(name = "campaign_id")
  private Long campaignId;

  @Column(name = "campaign_code")
  private String campaignCode;

  @Column(name = "rule_id")
  private Long ruleId;

  @Column(name = "rule_code")
  private String ruleCode;

  @Column(name = "transaction_at")
  private LocalDateTime transactionAt;

  @Column(name = "expire_at")
  private LocalDate expireAt;

  @Column(name = "search_transaction_date")
  private String searchTransactionDate;
}
