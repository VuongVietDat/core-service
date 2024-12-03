package vn.com.atomi.loyalty.core.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.gift.enums.TransactionStatus;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class GiftClaimOutput {

  @Schema(description = "Tên quà")
  private String name;

  @Schema(description = "Mã giao dịch trên loyalty")
  private String transactionId;

  @Schema(description = "Mã định danh của khách hàng trên bank")
  private String cifBank;

  @Schema(description = "Mã định danh của khách hàng trên ví")
  private String cifWallet;

  @Schema(description = "ID của quà")
  private Long giftId;

  @Schema(description = "Danh sách mã quà đã đổi")
  private List<String> voucherCodes;

  @Schema(description = "Số tham chiếu")
  private String refNo;

  @Schema(description = "SUCCESS: Thành công </br>FAILURE: Thất bại")
  private TransactionStatus transactionStatus;
}
