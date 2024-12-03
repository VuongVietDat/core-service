package vn.com.atomi.loyalty.core.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author thuongdd
 * @version 1.0
 */
@Setter
@Getter
public class RegisterPackageInput {
    @NotBlank(message = "Mã gói hội viên không được để trống")
    @Schema(description = "Mã gói hội viên ")
    private String packageCode;

    @NotBlank(message = "Tên gói hội viên không được để trống")
    @Schema(description = "Tên gói hội viên ")
    private String packageName;

    @NotNull(message = "Phí đăng ký không được để trống")
    @Schema(description = "Phí đăng ký hội viên ")
    @Min(value = 0, message = "Phí đăng ký phải lớn hơn hoặc bằng 0")
    private Long registrationFee;

    @NotNull(message = "Ngày hiệu lực không được để trống")
    @Schema(description = "Ngày hiệu lực format : dd/MM/yyyy ")
    private String startTime;

    @NotNull(message = "Ngày hết hạn không được để trống")
    @Schema(description = "Ngày hết hạn  format : dd/MM/yyyy")
    private String endTime;

    @NotNull(message = "Ảnh Thumbnail không được để trống")
    @Schema(description = "Mã gói hội viên ")
    private String thumbnail;

    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    @Schema(description = "Mô tả gói hội viên ")
    private String description;

    @NotBlank(message = "Trạng thái không được để trống")
    @Schema(description = "Trạng thái gói hội viên ")
    private String status;

    @NotBlank(message = "Trạng thái phê duyệt không được để trống")
    @Schema(description = "Trạng thái phê duyệt gói hội viên ")
    private String approvalStatus;

    @Schema(description = "Danh sách sản phẩm ")
    private List<Long> products;

    @Schema(description = "Danh sách ưu đãi từ LPB")
    private List<Long> gift;

    @Schema(description = "Danh sách ưu đãi từ đối tác ")
    private List<Long> giftPartner;

}