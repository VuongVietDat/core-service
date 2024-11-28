package vn.com.atomi.loyalty.core.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class PurchaseChainMissionInput {

  @Schema(description = "ID khách hàng bên loyalty")
  private Long customerId;

  @Schema(description = "Cif No tại corebank")
  private String cifNo;

  @Schema(description = "Số tham chiếu corebank")
  private String refNo;

  @Schema(description = "Số tham chuỗi nhiệm vụss")
  private Long chainId;

  @Schema(description = "ID giao dịch")
  private String transId;

  @Schema(description = "Ngày đăng ký  format : dd/MM/yyyy HH:mm:ss")
  private String transactionAt;

  @Schema(description = "Số tiền")
  private Long txnAmount;

  @Schema(description = "Loại tiền")
  private String txnCurrency;

  @Schema(description = "Phương thức thanh toán")
  private String paymentMethod;

  @Schema(description = "Kênh thanh toán")
  private String paymentChannel;

  @Schema(description = "Nội dung thanh toán")
  private String notes;

}
