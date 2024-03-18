package vn.com.atomi.loyalty.core.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.core.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class CustomerGroupInput {

  @Schema(description = "Tên nhóm khách hàng")
  @NotBlank
  private String name;

  @Schema(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
  @NotNull
  private Status status;

  @Schema(description = "Điều kiện nhóm khách hàng")
  @NotBlank
  private String builder;
}
