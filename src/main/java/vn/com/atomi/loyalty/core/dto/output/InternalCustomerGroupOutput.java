package vn.com.atomi.loyalty.core.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InternalCustomerGroupOutput {

  @Schema(description = "ID bản ghi")
  private Long id;

  @Schema(description = "Mã nhóm khách hàng")
  private String code;

  @Schema(description = "Tên nhóm khách hàng")
  private String name;
}
