package vn.com.atomi.loyalty.core.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class GetListBenefitOutput {

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
  private String urlImgage;

  @Schema(description = "Thư tự hiển thị")
  private Integer displayOrder;

  @Schema(description = "Ngày hiệu lực")
  private LocalDate effectiveDate;

  @Schema(description = "Ngày hết hạn")
  private LocalDate expriredDate;

  @Schema(description = "Mô tả gói hội viên")
  private Integer description;

}
