package vn.com.atomi.loyalty.core.dto.output;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link vn.com.atomi.loyalty.core.entity.PkgBenefit}
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PkgBenefitOutput implements Serializable {
    Long id;
    Long productLine;
    Long product;
    Float txnRefund;
    Long txnMaxAmount;
    Long custMaxAmount;
    String custRefundTime;
    String urlImage;
    String description;
}