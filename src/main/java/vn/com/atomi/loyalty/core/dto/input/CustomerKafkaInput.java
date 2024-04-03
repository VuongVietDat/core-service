package vn.com.atomi.loyalty.core.dto.input;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerKafkaInput {
  private String cifBank;
  private String cifWallet;
  private String customerName;
  private String dob;
  private String currentAddress;
  private String customerType;
  private String gender;
  private String nationality;
  private String ownerBranch;
  private String phone;
  private String rank;
  private String registerBranch;
  private String residentialAddress;
  private String rmCode;
  private String rmName;
  private String segment;
  private String uniqueType;
  private String uniqueValue;
  private String issueDate;
  private String issuePlace;
}
