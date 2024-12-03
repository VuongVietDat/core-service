package vn.com.atomi.loyalty.core.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import vn.com.atomi.loyalty.base.constant.DateConstant;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InternalGiftOutput {

  @Schema(description = "ID quà")
  private Long id;

  @Schema(description = "ID danh mục")
  private Long categoryId;

  @Schema(description = "Tên")
  private String name;

  @Schema(description = "Ngày bắt đầu hiệu lực (dd/MM/yyyy)", example = "01/01/2024")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private LocalDate startDate;

  @Schema(description = "Ngày kết thúc hiệu lực (dd/MM/yyyy)", example = "31/12/2024")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private LocalDate endDate;

  @Schema(description = "Tổng số quà")
  private Long giftCount;

  @Schema(description = "Tổng số lượng còn lại")
  private Long totalRemaining;

  @Schema(description = "Mô tả")
  private String description;

  @Schema(description = "Giới hạn quà/ người dùng")
  private Long limitGiftPerUser;

  @Schema(description = "Số điểm đổi")
  private Long price;

  @Schema(description = "Số lượng")
  private Long quantity;

  @Schema(description = "Hướng dẫn sử dụng")
  private String guide;

  @Schema(description = "Điều khoản sử dụng")
  private String termsOfUse;

  @Schema(description = "Hỗ trợ")
  private String support;

  @Schema(description = "Danh sách ảnh của quà")
  private String images;

  @Schema(description = "Ảnh nền của ưu đãi")
  private String thumbnail;

  @Schema(description = "Áp dụng với nhóm người dùng")
  private List<Long> customerGroupIds;

  @Schema(description = "Danh sách mã quà đã đổi")
  private List<String> voucherCodes;

  @Schema(description = "Danh sách địa chỉ áp dụng")
  private List<GiftApplyAddressOutput> applyAddress;
}
