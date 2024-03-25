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
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "C_CUSTOMER_RANK_ID_SEQ")
  @SequenceGenerator(
      name = "C_CUSTOMER_RANK_ID_SEQ",
      sequenceName = "C_CUSTOMER_RANK_ID_SEQ",
      allocationSize = 1)
  private Long id;

  @Column(name = "CODE")
  private String code;

  @Column(name = "CUSTOMER_ID")
  private Long customerId;

  @Column(name = "RANK")
  private String rank;

  @Column(name = "APPLY_DATE")
  private LocalDate applyDate;

  @Column(name = "TOTAL_POINT")
  private Long totalPoint;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private Status status;
}
