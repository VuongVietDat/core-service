package vn.com.atomi.loyalty.core.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;

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
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "c_customer_balance_id_seq")
  @SequenceGenerator(
      name = "c_customer_balance_id_seq",
      sequenceName = "c_customer_balance_id_seq",
      allocationSize = 1)
  private Long id;

  @Column(name = "customer_id")
  private Long customerId;

  @Column(name = "total_amount")
  private Long totalAmount;

  @Column(name = "lock_amount")
  private Long lockAmount;

  @Column(name = "available_amount")
  private Long availableAmount;
}
