package vn.com.atomi.loyalty.core.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.*;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.core.enums.ApprovalStatus;
import vn.com.atomi.loyalty.core.enums.ApprovalType;

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
public class HistoryOutput {

  @Schema(description = "ID bản ghi")
  private Long id;

  @Schema(description = "ID bản ghi phê duyệt")
  private Long approvalId;

  @Schema(
      description =
          "Trạng thái phê duyệt:</br> WAITING: Chờ duyệt</br> ACCEPTED: Đồng ý</br> REJECTED: Từ chối</br> RECALL: Thu hồi")
  private ApprovalStatus approvalStatus;

  @Schema(
      description =
          "Loại phê duyệt: </br>CREATE: Phê duyệt tạo</br>UPDATE: Phê duyệt cập nhật</br>CANCEL: Phê duyệt hủy bỏ")
  private ApprovalType approvalType;

  @Schema(description = "Lý do đồng ý hoặc từ chối")
  private String approvalComment;

  @Schema(description = "Người thực hiện")
  private String userAction;

  @Schema(description = "Thời điểm thao tác")
  @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS_STROKE)
  private LocalDateTime actionAt;
}
