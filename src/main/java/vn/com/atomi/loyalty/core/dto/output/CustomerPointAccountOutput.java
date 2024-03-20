package vn.com.atomi.loyalty.core.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.base.constant.DateConstant;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class CustomerPointAccountOutput {

  @Schema(description = "ID khách hàng trên hệ thống loyalty")
  private Long customerId;

  @Schema(description = "Tên khách hàng")
  private String customerName;

  @Schema(description = "Số giấy tờ tùy thân")
  private String uniqueValue;

  @Schema(description = "Mã định danh của khách hàng trên bank")
  private String cifBank;

  @Schema(description = "Mã định danh của khách hàng trên ví")
  private String cifWallet;

  @Schema(description = "Tổng điểm totalAmount = lockAmount + availableAmount")
  private Long totalAmount;

  @Schema(description = "Điểm bị khoá lockAmount = totalAmount - availableAmount")
  private Long lockAmount;

  @Schema(description = "Điểm hiệu lực availableAmount = totalAmount - lockAmount")
  private Long availableAmount;

  @Schema(description = "Hạng khách hàng")
  private String rank;

  @Schema(description = "Tổng điểm xếp hạng")
  private Long rankPoint;

  @Schema(description = "Số điểm gần hết hạn")
  private Long pointAboutExpire;

  @Schema(description = "Tổng điểm đã sử dụng")
  private Long totalPointsUsed;

  @Schema(description = "Ngày hết hạn điểm gần nhất (dd/MM/yyyy)")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private LocalDate mostRecentExpirationDate;
}
