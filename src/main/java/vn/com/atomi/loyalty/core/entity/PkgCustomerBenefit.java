package vn.com.atomi.loyalty.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

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

    @NotNull
    @Column(name = "PACKAGE_ID", nullable = false)
    private Long packageId;

    @NotNull
    @Column(name = "CUSTOMER_ID", nullable = false)
    private Long customer;

    @Column(name = "PARTNER_ID")
    private Long partner;

    @Column(name = "GIFT_PARTNER_ID")
    private Long giftPartner;

    @Column(name = "GIFT_QUANTITY")
    private Integer giftQuantity;

    @Size(max = 50)
    @Column(name = "CIF_NO", length = 50)
    private String cifNo;

    @Size(max = 10)
    @NotNull
    @Nationalized
    @Column(name = "STATUS", nullable = false, length = 10)
    private String status;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder;

}