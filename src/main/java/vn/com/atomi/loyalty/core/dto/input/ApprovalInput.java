package vn.com.atomi.loyalty.core.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class ApprovalInput {

  @Schema(description = "ID bản ghi chờ duyệt")
  @NotNull
  private Long id;

  @Schema(description = "Đồng ý (TRUE) hay từ chối (FALSE)")
  @NotNull
  private Boolean isAccepted;

  @Schema(description = "Lý do đồng ý hoặc từ chối")
  private String comment;
}
