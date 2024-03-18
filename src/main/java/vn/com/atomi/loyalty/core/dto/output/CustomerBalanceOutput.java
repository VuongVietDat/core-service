package vn.com.atomi.loyalty.core.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @author haidv
 * @version 1.0
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBalanceOutput {

  @Schema(description = "Tên khách hàng")
  private String customerName;

  @Schema(description = "Mã định danh của khách hàng trên bank")
  private String cifBank;

  @Schema(description = "Mã định danh của khách hàng trên ví")
  private String cifWallet;

  @Schema(description = "ID khách hàng trên hệ thống loyalty")
  private Long customerId;

  @Schema(description = "Tổng điểm totalAmount = lockAmount + availableAmount")
  private Long totalAmount;

  @Schema(description = "Điểm bị khoá lockAmount = totalAmount - availableAmount")
  private Long lockAmount;

  @Schema(description = "Điểm hiệu lực availableAmount = totalAmount - lockAmount")
  private Long availableAmount;
}
