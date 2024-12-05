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
public class GetListCustomerBenefitOutput {
  @Schema(description = "ID bản ghi")
  private Long id;

  @Schema(description = "ID gói hội viên")
  private String customerId;

  @Schema(description = "ID gói hội viên")
  private String packageId;

  @Schema(description = "Tên ưu đãi")
  private String name;

  @Schema(description = "Loại ưu đãi")
  private String type;

  @Schema(description = "Số lượng ưu đãi")
  private String quantity;

  @Schema(description = "Ngày hiệu lực")
  private String startDate;

  @Schema(description = "Ngày hết hạn")
  private String endDate;

  @Schema(description = "Trạng thái")
  private String status;

  @Schema(description = "Thứ tự hiển thị")
  private Integer displayOrder;

  @Schema(description = "Đường dẫn ảnh")
  private String urlImage;

  @Schema(description = "Mô tả")
  private String description;


}
