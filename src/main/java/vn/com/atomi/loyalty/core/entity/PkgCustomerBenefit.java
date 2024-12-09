package vn.com.atomi.loyalty.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import vn.com.atomi.loyalty.core.enums.Status;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PKG_CUSTOMER_BENEFIT")
public class PkgCustomerBenefit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PKG_CUSTOMER_BENEFIT_id_gen")
    @SequenceGenerator(name = "PKG_CUSTOMER_BENEFIT_id_gen", sequenceName = "PKG_CBT_ID_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "PACKAGE_ID", nullable = false)
    private Long packageId;

    @Column(name = "CUSTOMER_ID", nullable = false)
    private Long customerId;

    @Column(name = "PARTNER_ID")
    private Long partnerId;

    @Column(name = "GIFT_PARTNER_ID")
    private Long giftPartnerId;

    @Column(name = "GIFT_QUANTITY")
    private Integer giftQuantity;

    @Size(max = 50)
    @Column(name = "CIF_NO", length = 50)
    private String cifNo;

    @Size(max = 10)
    @Nationalized
    @Column(name = "STATUS", nullable = false, length = 10)
    private String status;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder;

    @Column(name = "TXN_DATE")
    private LocalDate txnDate;

    @Column(name = "EFFECTIVE_DATE")
    private LocalDate effectiveDate;

    @Column(name = "EXPIRE_DATE")
    private LocalDate expireDate;
}