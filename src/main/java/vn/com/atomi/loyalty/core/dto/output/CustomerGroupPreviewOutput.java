package vn.com.atomi.loyalty.core.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.core.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class CustomerGroupPreviewOutput {

  @Schema(description = "ID bản ghi")
  private Long id;

  @Schema(description = "Mã quy tắc sinh điểm")
  private String code;

  @Schema(description = "Tên quy tắc sinh điểm")
  private String name;

  @Schema(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
  private Status status;

  @Schema(description = "Người tạo")
  private String creator;

  @Schema(description = "Ngày tạo (dd/MM/yyyy HH:mm:ss)")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS_STROKE)
  private LocalDateTime creationDate;

  @Schema(description = "Ngày duyệt tạo (dd/MM/yyyy HH:mm:ss)")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS_STROKE)
  private LocalDateTime creationApprovalDate;
}
