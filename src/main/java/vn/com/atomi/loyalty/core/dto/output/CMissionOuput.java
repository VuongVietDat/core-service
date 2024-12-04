package vn.com.atomi.loyalty.core.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link vn.com.atomi.loyalty.core.entity.CMission}
 */
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CMissionOuput implements Serializable {

    @Schema(description = "ID nhiệm vụ")
    private Long id;

    @Schema(description = "ID chuỗi nhiệm vụ")
    private Long chainId;

    @Schema(description = "Thứ tự hiển thị")
    private Integer orderNo;

    @Schema(description = "Mã nhiệm vụ")
    private String code;

    @Schema(description = "Tên nhiệm vụ")
    private  String name;

    @Schema(description = "0 | Điểm Loyalty, 1 | e-Voucher, 2 | Gift")
    private String benefitType;

    @Schema(description = "Ngày bắt đầu")
    private String startDate;

    @Schema(description = "Ngày kết thúc")
    private String endDate;

    @Schema(description = "Phí đăng ký")
    private BigDecimal price;

    @Schema(description = "Loại phí")
    private String currency;

    @Schema(description = "Đường dẫn ảnh")
    private String image;

    @Schema(description = "Mô tả nhiệm vụ")
    private  String notes;

    @Schema(description = "0 | OR, 1 | AND")
    private  String groupType;

    @Schema(description = "Trạng thái thực hiện")
    private  String status;
}