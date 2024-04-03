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
  @Column(name = "ID")
  private Long id;

  @Column(name = "CIF_BANK")
  private String cifBank;

  @Column(name = "CIF_WALLET")
  private String cifWallet;

  @Column(name = "CUSTOMER_NAME")
  private String customerName;

  @Column(name = "DOB")
  private LocalDate dob;

  @Column(name = "CURRENT_ADDRESS")
  private String currentAddress;

  @Column(name = "CUSTOMER_TYPE")
  private String customerType;

  @Column(name = "GENDER")
  private String gender;

  @Column(name = "NATIONALITY")
  private String nationality;

  @Column(name = "OWNER_BRANCH")
  private String ownerBranch;

  @Column(name = "PHONE")
  private String phone;

  @Column(name = "RANK")
  private String rank;

  @Column(name = "REGISTER_BRANCH")
  private String registerBranch;

  @Column(name = "RESIDENTIAL_ADDRESS")
  private String residentialAddress;

  @Column(name = "RM_CODE")
  private String rmCode;

  @Column(name = "RM_NAME")
  private String rmName;

  @Column(name = "SEGMENT")
  private String segment;

  @Column(name = "UNIQUE_TYPE")
  private String uniqueType;

  @Column(name = "UNIQUE_VALUE")
  private String uniqueValue;

  @Column(name = "ISSUE_DATE")
  private LocalDate issueDate;

  @Column(name = "ISSUE_PLACE")
  private String issuePlace;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private Status status;
}
