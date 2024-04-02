package vn.com.atomi.loyalty.core.dto.input;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.core.enums.ChangeType;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class UsePointInput {

  @Schema(description = "ID khách hàng bên loyalty")
  @NotNull
  private Long customerId;

  @Schema(description = "Số tham chiếu")
  @NotBlank
  private String refNo;

  @JsonIgnore
  @Schema(description = "Loại thay đổi: </br>PLUS: Cộng điểm</br>PLUS: Trừ điểm")
  @NotNull
  private ChangeType changeType;

  @Schema(description = "Số point thay đổi")
  @NotNull
  private Long amount;
}
