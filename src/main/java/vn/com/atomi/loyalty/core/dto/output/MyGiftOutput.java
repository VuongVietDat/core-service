package vn.com.atomi.loyalty.core.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.gift.enums.VoucherStatus;

import java.time.LocalDate;

/**
 * @author haidv
 * @version 1.0
 */
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MyGiftOutput {

  @Schema(description = "ID lịch sử đổi quà")
  private Long id;

  @Schema(description = "ID quà")
  private Long giftId;

  @Schema(description = "Tên")
  private String name;

  @Schema(description = "Ngày kết thúc hiệu lực (dd/MM/yyyy)", example = "31/12/2024")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private LocalDate endDate;

  @Schema(description = "Tổng điểm đổi")
  private Long totalPoint;

  @Schema(description = "Số lượng")
  private Long quantity;

  @Schema(description = "Ảnh nền của ưu đãi")
  private String thumbnail;

  @Schema(description = "Số điểm đổi")
  private Long price;

  @Schema(description = "Thời gian đổi")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private LocalDate claimsAt;

  @Schema(
      description = "AVAILABLE: Chưa dùng</br>USED: Đã dùng</br>EXPIRED: Hết hạn</br>CLAIMED: Đã dùng point để claims gift")
  private VoucherStatus voucherStatus;
}
