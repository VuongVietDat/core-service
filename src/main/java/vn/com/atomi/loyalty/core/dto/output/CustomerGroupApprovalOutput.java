package vn.com.atomi.loyalty.core.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.core.enums.ApprovalStatus;
import vn.com.atomi.loyalty.core.enums.ApprovalType;
import vn.com.atomi.loyalty.core.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class CustomerGroupApprovalOutput {

  @Schema(description = "ID bản ghi")
  private Long id;

  @Schema(description = "Mã nhóm khách hàng")
  private String code;

  @Schema(description = "Tên nhóm khách hàng")
  private String name;

  @Schema(description = "Điều kiện nhóm khách hàng")
  private String builder;

  @Schema(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
  private Status status;

  @Schema(
      description =
          "Trạng thái phê duyệt:</br> WAITING: Chờ duyệt</br> ACCEPTED: Đồng ý</br> REJECTED: Từ chối</br> RECALL: Thu hồi")
  private ApprovalStatus approvalStatus;

  @Schema(description = "Người tạo")
  private String creator;

  @Schema(description = "Ngày tạo")
  private LocalDateTime creationDate;

  @Schema(description = "Ngày duyệt")
  private LocalDateTime approveDate;

  @Schema(
      description =
          "Loại phê duyệt: </br>CREATE: Phê duyệt tạo</br>UPDATE: Phê duyệt cập nhật</br>CANCEL: Phê duyệt hủy bỏ")
  private ApprovalType approvalType;

  @Schema(description = "Người duyệt")
  private String approver;

  @Schema(description = "Lý do đồng ý hoặc từ chối")
  private String approvalComment;
}
