package vn.com.atomi.loyalty.core.dto.output;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link vn.com.atomi.loyalty.core.entity.PkgCustomerBenefit}
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PkgCustomerBenefitOutput implements Serializable {

    private Long id;

    private Long packageId;

    private Long customerId;

    private Long giftPartnerId;

    private String name;

    private String status;

    private String type;

    private Integer quantity;

    private String startDate;

    private String endDate;

    private Integer displayOrder;

    private String urlImage;

    private String description;
}