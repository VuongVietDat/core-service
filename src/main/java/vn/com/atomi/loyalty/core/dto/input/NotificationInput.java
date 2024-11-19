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
public class NotificationInput {
  @Schema(description = "Ngôn ngữ (mặc định là VN)")
  @NotNull
  private String language;
  @Schema(description = "Thời gian gửi từ client: yyyyMMddHHmmss.SSS")
  @NotNull
  private String clientTime;
  @Schema(description = "ID request")
  @NotNull
  private String requestId;
  @Schema(description = "Thời gian thực hiện")
  @NotNull
  private String transTime;
  @Schema(description = "Tiêu đề thông báo")
  @NotNull
  private String title;
  @Schema(description = "Nội dung thông báo")
  @NotNull
  private String content;
  @Schema(description = "Số điện thoại người nhận")
  @NotNull
  private String userName;
}

