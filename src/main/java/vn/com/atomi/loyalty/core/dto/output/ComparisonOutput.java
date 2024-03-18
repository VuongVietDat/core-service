package vn.com.atomi.loyalty.core.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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
public class ComparisonOutput {

  @Schema(description = "Tên trường")
  private String fileName;

  @Schema(description = "Giá trị cũ")
  private String oldValue;

  @Schema(description = "Giá trị mới")
  private String newValue;
}
