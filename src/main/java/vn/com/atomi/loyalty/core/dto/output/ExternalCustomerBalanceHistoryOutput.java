package vn.com.atomi.loyalty.core.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.*;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.core.enums.ChangeType;

/**
 * @author haidv
 * @version 1.0
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExternalCustomerBalanceHistoryOutput {

  @Schema(
      description =
          "Loại thay đổi: </br>PLUS: Cộng điểm</br>MINUS_CONSUMPTION: Trừ điểm tiêu dùng</br>MINUS_EXPIRED: Trừ điểm hết hạn")
  private ChangeType changeType;

  @Schema(description = "Số điểm thay đổi")
  private Long amount;

  @Schema(description = "Số điểm trước thay đổi")
  private Long beforeAmount;

  @Schema(description = "Số điểm sau thay đổi")
  private Long afterAmount;

  @Schema(description = "Số tham chiếu")
  private String refNo;

  @Schema(description = "Thời gian giao dịch")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS_STROKE)
  private LocalDateTime transactionAt;
}
