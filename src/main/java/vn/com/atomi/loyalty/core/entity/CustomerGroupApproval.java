package vn.com.atomi.loyalty.core.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.core.enums.ApprovalStatus;
import vn.com.atomi.loyalty.core.enums.ApprovalType;
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
@Table(name = "C_CUSTOMER_GROUP_APPROVAL")
public class CustomerGroupApproval extends BaseEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "c_customer_group_arv_id_seq")
  @SequenceGenerator(
      name = "c_customer_group_arv_id_seq",
      sequenceName = "c_customer_group_arv_id_seq",
      allocationSize = 1)
  private Long id;

  @Column(name = "customer_group_id")
  private Long customerGroupId;

  @Column(name = "code")
  private String code;

  @Column(name = "name")
  private String name;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "builder")
  private String builder;

  @Column(name = "approval_status")
  @Enumerated(EnumType.STRING)
  private ApprovalStatus approvalStatus;

  @Column(name = "approval_type")
  @Enumerated(EnumType.STRING)
  private ApprovalType approvalType;

  @Column(name = "approval_comment")
  private String approvalComment;
}
