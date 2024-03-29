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
  private Long id;

  @Column(name = "CODE")
  private String code;

  @Column(name = "CUSTOMER_ID")
  private Long customerId;

  @Column(name = "TOTAL_AMOUNT")
  private long totalAmount;

  @Column(name = "LOCK_AMOUNT")
  private long lockAmount;

  @Column(name = "AVAILABLE_AMOUNT")
  private long availableAmount;

  @Column(name = "TOTAL_POINTS_USED")
  private long totalPointsUsed;

  @Column(name = "TOTAL_ACCUMULATED_POINTS")
  private long totalAccumulatedPoints;

  @Column(name = "TOTAL_POINTS_EXPIRED")
  private long totalPointsExpired;

  @Column(name = "LST_YEAR_ACCUMULATED")
  private int lstYearAccumulated;

  @Column(name = "YEAR_ACCUMULATED")
  private long yearAccumulated;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private Status status;
}
