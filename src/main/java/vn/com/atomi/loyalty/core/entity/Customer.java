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
@Table(name = "C_CUSTOMER")
public class Customer extends BaseEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "c_customer_id_seq")
  @SequenceGenerator(
      name = "c_customer_id_seq",
      sequenceName = "c_customer_id_seq",
      allocationSize = 1)
  private Long id;

  @Column(name = "cif_bank")
  private String cifBank;

  @Column(name = "cif_wallet")
  private String cifWallet;

  @Column(name = "current_address")
  private String currentAddress;

  @Column(name = "customer_name")
  private String customerName;

  @Column(name = "customer_type")
  private String customerType;

  @Column(name = "gender")
  private String gender;

  @Column(name = "nationality")
  private String nationality;

  @Column(name = "owner_branch")
  private String ownerBranch;

  @Column(name = "phone")
  private String phone;

  @Column(name = "rank")
  private String rank;

  @Column(name = "register_branch")
  private String registerBranch;

  @Column(name = "residential_address")
  private String residentialAddress;

  @Column(name = "rm_code")
  private String rmCode;

  @Column(name = "rm_name")
  private String rmName;

  @Column(name = "segment")
  private String segment;

  @Column(name = "unique_type")
  private String uniqueType;

  @Column(name = "unique_value")
  private String uniqueValue;

  @Column(name = "issue_date")
  private LocalDate issueDate;

  @Column(name = "issue_place")
  private String issuePlace;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;
}
