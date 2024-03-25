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
  @Column(name = "id")
  private Long id;

  @Column(name = "code")
  private String code;

  @Column(name = "customer_id")
  private Long customerId;

  @Column(name = "total_amount")
  private Long totalAmount;

  @Column(name = "lock_amount")
  private Long lockAmount;

  @Column(name = "available_amount")
  private Long availableAmount;

  @Column(name = "total_points_used")
  private Long totalPointsUsed;

  @Column(name = "total_accumulated_points")
  private Long totalAccumulatedPoints;

  @Column(name = "total_points_expired")
  private Long totalPointsExpired;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;
}
