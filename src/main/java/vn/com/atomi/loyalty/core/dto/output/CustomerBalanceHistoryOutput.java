package vn.com.atomi.loyalty.core.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.core.enums.ChangeType;
import vn.com.atomi.loyalty.core.enums.PointType;

/**
 * @author haidv
 * @version 1.0
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBalanceHistoryOutput {

  @Schema(description = "ID bản ghi")
  private Long id;

  @Schema(description = "ID khách hàng trên hệ thống loyalty")
  private Long customerId;

  @Schema(
      description =
          "Loại thay đổi: </br>PLUS: Cộng điểm</br>MINUS_CONSUMPTION: Trừ điểm tiêu dùng</br>MINUS_EXPIRED: Trừ điểm hết hạn")
  private ChangeType changeType;

  @Schema(
      description =
          "Loại điểm: </br>RANK_POINT: Điểm xếp hạng</br>CONSUMPTION_POINT: Điểm tiêu dùng")
  private PointType pointType;

  @Schema(description = "Số điểm thay đổi")
  private Long amount;

  @Schema(description = "Số điểm trước thay đổi")
  private Long beforeAmount;

  @Schema(description = "Số điểm sau thay đổi")
  private Long afterAmount;

  @Schema(description = "Số tham chiếu")
  private String refNo;

  @Schema(description = "Mã giao dịch")
  private Long transactionId;

  @Schema(description = "Nội dung")
  private String content;

  @Schema(description = "ID chiến dịch")
  private Long campaignId;

  @Schema(description = "Mã chiến dịch")
  private String campaignCode;

  @Schema(description = "ID công thức sinh điểm")
  private Long ruleId;

  @Schema(description = "Mã công thức sinh điểm")
  private String ruleCode;

  @Schema(description = "Thời gian giao dịch")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS_STROKE)
  private LocalDateTime transactionAt;

  @Schema(description = "Thời gian hết hạn")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private LocalDate expireAt;
}
