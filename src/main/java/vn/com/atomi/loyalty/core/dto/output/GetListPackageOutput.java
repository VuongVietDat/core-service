package vn.com.atomi.loyalty.core.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.core.enums.ApprovalStatus;
import vn.com.atomi.loyalty.core.enums.ApprovalType;
import vn.com.atomi.loyalty.core.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class GetListPackageOutput {

  @Schema(description = "ID bản ghi")
  private Long id;

  @Schema(description = "ID gói hội viên")
  private String packageId;

  @Schema(description = "Tên ưu đãi")
  private String name;

  @Schema(description = "Loại ưu đãi")
  private String type;

  @Schema(description = "Thứ tự hiển thị")
  private String displayOrder;

  @Schema(description = "Đường dẫn ảnh")
  private Integer urlImgage;

  @Schema(description = "Mô tả")
  private String description;

}
