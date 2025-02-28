package vn.com.atomi.loyalty.core.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class CustomerPointAccountPreviewOutput {

  @Schema(description = "ID khách hàng trên hệ thống loyalty")
  private Long customerId;

  @Schema(description = "Tên khách hàng")
  private String customerName;

  @Schema(description = "Loại giấy tờ tùy thân")
  private String uniqueType;

  @Schema(description = "Tên loại giấy tờ tùy thân")
  private String uniqueTypeName;

  @Schema(description = "Số giấy tờ tùy thân")
  private String uniqueValue;

  @Schema(description = "Mã định danh của khách hàng trên bank")
  private String cifBank;

  @Schema(description = "Mã định danh của khách hàng trên ví")
  private String cifWallet;

  @Schema(description = "Điểm hiệu lực availableAmount = totalAmount - lockAmount")
  private Long availableAmount;

  @Schema(description = "Tổng điểm xếp hạng")
  private Long rankPoint;
}
