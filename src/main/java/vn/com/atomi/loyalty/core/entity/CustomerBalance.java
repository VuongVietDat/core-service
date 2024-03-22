package vn.com.atomi.loyalty.core.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.core.enums.Status;

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
@Table(name = "C_CUSTOMER_BALANCE")
public class CustomerBalance extends BaseEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "C_CUSTOMER_BALANCE_ID_SEQ")
  @SequenceGenerator(
      name = "C_CUSTOMER_BALANCE_ID_SEQ",
      sequenceName = "C_CUSTOMER_BALANCE_ID_SEQ",
      allocationSize = 1)
  private Long id;

  @Column(name = "CODE")
  private String code;

  @Column(name = "CUSTOMER_ID")
  private Long customerId;

  @Column(name = "TOTAL_AMOUNT")
  private Long totalAmount;

  @Column(name = "LOCK_AMOUNT")
  private Long lockAmount;

  @Column(name = "AVAILABLE_AMOUNT")
  private Long availableAmount;

  @Column(name = "TOTAL_POINTS_USED")
  private Long totalPointsUsed;

  @Column(name = "TOTAL_ACCUMULATED_POINTS")
  private Long totalAccumulatedPoints;

  @Column(name = "TOTAL_POINTS_EXPIRED")
  private Long totalPointsExpired;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private Status status;
}
