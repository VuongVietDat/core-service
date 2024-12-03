package vn.com.atomi.loyalty.core.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class ClaimGiftInput {

  @Schema(description = "Mã định danh của khách hàng trên bank")
  @NotBlank
  private String cifBank;

  @Schema(description = "Mã định danh của khách hàng trên ví")
  @NotBlank
  private String cifWallet;

  @Schema(description = "ID của quà")
  @NotNull
  private Long giftId;

  @Schema(description = "Số điểm đổi")
  @NotNull
  @Min(0)
  private Long price;

  @Schema(description = "Số lượng")
  @NotNull
  @Min(1)
  private Long quantity;

  @Schema(description = "Số tham chiếu")
  @NotBlank
  private String refNo;

  @Schema(description = "TRUE: Chỉ thực hiện validate, FALSE: Không thực hiện đổi quà")
  @NotBlank
  private boolean validation;
}
