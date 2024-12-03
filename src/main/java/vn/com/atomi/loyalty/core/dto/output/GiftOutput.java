package vn.com.atomi.loyalty.core.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.gift.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GiftOutput {

  @Schema(description = "ID quà")
  private Long id;

  @Schema(description = "ID danh mục")
  @NotNull
  private Long categoryId;

  @Schema(description = "Tên")
  private String name;

  @Schema(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
  private Status status;

  @Schema(description = "Người tạo")
  private String creator;

  @Schema(description = "Ngày tạo (dd/MM/yyyy HH:mm:ss)")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS_STROKE)
  private LocalDateTime creationDate;

  @Schema(description = "Loại quà", example = "Internal Coupon")
  @NotBlank
  private String type;

  @Schema(description = "Dịch vụ áp dụng", example = "Transfer")
  @NotBlank
  private String service;

  @Schema(description = "Loại giảm giá", example = "Percentage")
  @NotBlank
  private String discountType;

  @Schema(description = "Giá trị giảm")
  @NotBlank
  private String discountValue;

  @Schema(description = "Ngày bắt đầu hiệu lực (dd/MM/yyyy)", example = "01/01/2024")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private LocalDate startDate;

  @Schema(description = "Ngày kết thúc hiệu lực (dd/MM/yyyy)", example = "31/12/2024")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private LocalDate endDate;

  @Schema(description = "Giảm tối đa")
  @NotBlank
  private Long discountMax;

  @Schema(description = "Tổng số quà")
  @NotBlank
  private Long giftCount;

  @Schema(description = "Mô tả")
  @NotBlank
  private String description;

  @Schema(description = "Giá trị đơn hàng tối thiểu")
  @NotBlank
  private Long billValueMin;

  @Schema(description = "Giá trị đơn hàng tối đa")
  @NotBlank
  private Long billValueMax;

  @Schema(description = "Ngày bắt đầu bán (dd/MM/yyyy)", example = "01/01/2024")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private LocalDate saleStartDate;

  @Schema(description = "Ngày kết thúc bán (dd/MM/yyyy)", example = "01/01/2024")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private LocalDate saleEndDate;

  @NotBlank
  @Schema(description = "Hạng được mua", example = "Gold")
  private String rank;

  @Schema(description = "Cần phê duyệt quà")
  private Boolean needApproval;

  @Schema(description = "Giới hạn quà/ người dùng")
  private Long limitGiftPerUser;

  @Schema(description = "Số điểm đổi")
  private Long price;

  @Schema(description = "Áp dụng với nhóm người dùng")
  @NotNull
  private List<Long> customerGroupIds;
}
