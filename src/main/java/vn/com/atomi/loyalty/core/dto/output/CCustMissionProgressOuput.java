package vn.com.atomi.loyalty.core.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link vn.com.atomi.loyalty.core.entity.CCustMissionProgress}
 */
@Value
public class CCustMissionProgressOuput implements Serializable {

    @Schema(description = "ID quá trình thực hiện")
    private Long id;

    @Schema(description = "ID khách hàng ")
    private Long customer;

    @Schema(description = "ID bảng chuỗi nhiệm vụ")
    private Integer chain;

    @Schema(description = "ID nhiệm vụ")
    private Integer mission;

    @Schema(description = "Trạng thái của nhiệm vụ (PENDING, INPROGRESS, COMPLETED).")
    private String status;

    @Schema(description = "0 | OR, 1 | AND")
    private Boolean groupType;

    @Schema(description = "Thứ tự thực hiện nhiệm vụ trong chuỗi.")
    private Short orderNo;

    @Schema(description = "Thời gian bắt đầu nhiệm vụ/chuỗi nhiệm vụ.")
    private LocalDate startDate;

    @Schema(description = "Thời gian kết thúc nhiệm vụ/chuỗi nhiệm vụ.")
    private LocalDate endDate;

    @Schema(description = "Thời gian hoàn thành nhiệm vụ")
    private LocalDate completedAt;
}