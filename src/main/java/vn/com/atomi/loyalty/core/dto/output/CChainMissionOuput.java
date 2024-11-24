package vn.com.atomi.loyalty.core.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import vn.com.atomi.loyalty.core.enums.Chain;
import vn.com.atomi.loyalty.core.enums.Status;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link vn.com.atomi.loyalty.core.entity.CChainMission}
 */
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CChainMissionOuput implements Serializable {

    @Schema(description = "ID chuỗi nhiệm vụ")
    private Integer id;

    @Schema(description = "Mã chuỗi nhiệm vụ")
    private String code;

    @Schema(description = "Tên chuỗi nhiệm vụ")
    private String name;

    @Schema(description = "Trang thai: ACTIVE,INACTIVE")
    private Status status;

    @Schema(description = "0 | Điểm Loyalty, 1 | e-Voucher, 2 | Gift")
    private String benefitType;

    @Schema(description = "Ngày bắt đầu")
    private String startDate;

    @Schema(description = "Ngày kết thúc")
    private String endDate;

    @Schema(description = "Giá")
    private BigDecimal price;

    @Schema(description = "VND, USD, PNT")
    private String currency;

    @Schema(description = "N | No ordered, Y | Ordered")
    private String isOrdered;

    @Schema(description = "0 | OR, 1 | AND")
    private String groupType;

    @Schema(description ="Đường dẫn ảnh")
    private String image;

    @Schema(description = "Ghi chú")
    private String notes;

}