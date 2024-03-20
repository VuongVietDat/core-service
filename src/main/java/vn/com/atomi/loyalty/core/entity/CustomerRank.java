package vn.com.atomi.loyalty.core.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
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
@Table(name = "C_CUSTOMER_RANK")
public class CustomerRank extends BaseEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "c_customer_rank_id_seq")
  @SequenceGenerator(
      name = "c_customer_rank_id_seq",
      sequenceName = "c_customer_rank_id_seq",
      allocationSize = 1)
  private Long id;

  @Column(name = "customer_id")
  private Long customerId;

  @Column(name = "rank")
  private String rank;

  @Column(name = "apply_date")
  private LocalDate applyDate;

  @Column(name = "total_point")
  private Long totalPoint;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;
}
