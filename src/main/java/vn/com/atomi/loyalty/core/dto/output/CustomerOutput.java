package vn.com.atomi.loyalty.core.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Data;
import vn.com.atomi.loyalty.core.enums.Status;

@Data
public class CustomerOutput {

  @Schema(description = "Mã khách hàng")
  private Long id;

  @Schema(description = "Tên khách hàng")
  private String customerName;

  @Schema(description = "Ngày sinh")
  private LocalDate dob;

  @Schema(description = "CIF ngân hàng")
  private String cifBank;

  @Schema(description = "Số ví")
  private String cifWallet;

  @Schema(description = "Địa chỉ hiện tại")
  private String currentAddress;

  @Schema(description = "Phân loại khách hàng")
  private String customerType;

  @Schema(description = "Giới tính")
  private String gender;

  @Schema(description = "Quốc tịch")
  private String nationality;

  @Schema(description = "Chi nhánh quản lý")
  private String ownerBranch;

  @Schema(description = "Số điện thoại")
  private String phone;

  @Schema(description = "Hạng: Vàng, Bạc,...")
  private String rank;

  @Schema(description = "Chi nhánh đăng ký")
  private String registerBranch;

  @Schema(description = "Địa chỉ thường trú")
  private String residentialAddress;

  @Schema(description = "Mã nhân viên chăm sóc")
  private String rmCode;

  @Schema(description = "Tên nhân viên chăm sóc")
  private String rmName;

  @Schema(description = "Phân khúc")
  private String segment;

  @Schema(description = "Kiểu độc nhất")
  private String uniqueType;

  @Schema(description = "Giá trị duy nhất")
  private String uniqueValue;

  @Schema(description = "Ngày phát hành")
  private LocalDate issueDate;

  @Schema(description = "Nơi phát hành")
  private String issuePlace;

  @Schema(description = "Trạng thái")
  private Status status;
}
