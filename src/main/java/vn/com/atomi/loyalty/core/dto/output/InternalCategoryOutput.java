package vn.com.atomi.loyalty.core.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import vn.com.atomi.loyalty.gift.enums.CategoryType;

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
public class InternalCategoryOutput {

  @Schema(description = "ID danh mục")
  private Long id;

  @Schema(description = "Tên danh mục")
  private String name;

  @Schema(description = "Mã danh mục")
  private String code;

  @Schema(description = "Thứ tự sắp xếp")
  private Long orderNo;

  @Schema(description = "Loại danh mục:</br> STANDARD: Tiêu chuẩn</br> PRIVATE: Nâng cao")
  private CategoryType categoryType;
}
