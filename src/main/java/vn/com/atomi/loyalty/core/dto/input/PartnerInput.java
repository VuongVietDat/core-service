package vn.com.atomi.loyalty.core.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import vn.com.atomi.loyalty.core.enums.Status;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerInput {
    @Schema(description = "Mã đối tác")
    private String code;

    @Schema(description = "Tên công ty/ đối tác")
    private String name;

    @Schema(description = "Tên công ty/ đối tác tiếng Anh")
    private String nameEn;

    @Schema(description = "Mã số thuế")
    private Integer taxCode;

    @Schema(description = "Số điện thoại")
    private String phone;

    @Schema(description = "Email")
    private String email;

    @Schema(description = "Địa chỉ công ty")
    private String address;

    @Schema(description = "Người đại diện")
    private String represensative;

    @Schema(description = "Ngày bắt đầu hiệu lực")
    private String startDate;

    @Schema(description = "Ngày kết thúc hiệu lực")
    private String endDate;

    @Schema(description = "Trạng thái")
    private Status status;

    @Schema(description = "Đối tác nội bộ")
    private String isLocal;
}


