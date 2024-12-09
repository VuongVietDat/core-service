package vn.com.atomi.loyalty.core.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.core.entity.PkgBenefit;
import vn.com.atomi.loyalty.core.enums.ApprovalStatus;
import vn.com.atomi.loyalty.core.enums.ApprovalType;
import vn.com.atomi.loyalty.core.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class GetListPackageOutput {

  @Schema(description = "ID bản ghi")
  private Long id;

  @Schema(description = "Mã gói hội viên")
  private String code;

  @Schema(description = "Tên gói hội viên")
  private String name;

  @Schema(description = "Phí tham gia gói hội viên")
  private Integer fee;

  @Schema(description = "Loại tiền phí")
  private String currency;

  @Schema(description = "Đường dẫn ảnh")
  private String urlImage;

  @Schema(description = "Thư tự hiển thị")
  private Integer displayOrder;

  @Schema(description = "Ngày hiệu lực")
  private String effectiveDate;

  @Schema(description = "Ngày hết hạn")
  private String expriredDate;

  @Schema(description = "Mô tả gói hội viên")
  private String description;

  @Schema(description = "Danh sách ưu đãi của gói")
  private List<PkgBenefit> pkgBenefit;


}
