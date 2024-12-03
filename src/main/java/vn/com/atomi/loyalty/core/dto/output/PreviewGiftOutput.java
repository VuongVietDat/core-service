package vn.com.atomi.loyalty.core.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.gift.enums.Status;

import java.time.LocalDate;
import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class PreviewGiftOutput {

  @Schema(description = "Tên quà")
  private String name;

  @Schema(description = "Điều khoản sử dụng")
  private String termsOfUse;

  @Schema(description = "Hướng dẫn sử dụng")
  private String guide;

  @Schema(description = "Mô tả chi tiết")
  private String description;

  @Schema(description = "Hỗ trợ")
  private String support;

  @Schema(description = "Danh sách ảnh của quà")
  private List<String> images;

  @Schema(description = "Ảnh nền của ưu đãi")
  private String thumbnail;

  @Schema(description = "ID quà")
  private Long id;

  @Schema(description = "ID danh mục")
  @NotNull
  private Long categoryId;

  @Schema(description = "Tên danh mục")
  @NotNull
  private String categoryName;

  @Schema(description = "Tổng số quà")
  @NotBlank
  private Long giftCount;

  @Schema(description = "Ngày kết thúc hiệu lực (dd/MM/yyyy)", example = "31/12/2024")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private LocalDate endDate;

  @Schema(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
  private Status status;

  @Schema(description = "Người tạo")
  private String creator;
}
