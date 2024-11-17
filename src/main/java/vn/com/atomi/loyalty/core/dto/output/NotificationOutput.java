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
public class NotificationOutput {

  @Schema(description = "Reference Id")
  private String refId;

  @Schema(description = "Mã lỗi")
  private String resultCode;

  @Schema(description = "Mô tả lỗi")
  private String resultDesc;
}

