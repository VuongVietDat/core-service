package vn.com.atomi.loyalty.core.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class InternalCustomerGroupOutput {

  @Schema(description = "ID bản ghi")
  private Long id;

  @Schema(description = "Mã nhóm khách hàng")
  private String code;

  @Schema(description = "Tên nhóm khách hàng")
  private String name;
}
