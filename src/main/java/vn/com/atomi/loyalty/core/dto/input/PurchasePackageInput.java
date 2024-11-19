package vn.com.atomi.loyalty.core.dto.input;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.core.enums.ChangeType;

import java.sql.Date;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class PurchasePackageInput {

  @Schema(description = "ID khách hàng bên loyalty")
  private Long customerId;

  @Schema(description = "Cif No tại corebank")
  private String cifNo;

  @Schema(description = "ID gói hội viên")
  private Long packageId;

  @Schema(description = "Số tham chiếu corebank")
  private String refNo;

  @Schema(description = "ID giao dịch")
  private String transId;

  @Schema(description = "Ngày đăng ký")
  private Date purchasedDate;

  @Schema(description = "Ngày hiệu lực")
  private Date effectiveDate;

  @Schema(description = "Ngày hết hạn")
  private Date expiredDate;

  @Schema(description = "Số tiền")
  private Long txnAmount;

  @Schema(description = "Trạng thái đăng ký")
  private String txnStatus;

  @Schema(description = "Loại tiền")
  private String txnCurrency;

  @Schema(description = "Phương thức thanh toán")
  private String paymentMethod;

  @Schema(description = "Kênh thanh toán")
  private String paymentChannel;

}
