package vn.com.atomi.loyalty.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PKG_CUSTOMER_BENEFIT")
public class PkgCustomerBenefit {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PKG_CBT_ID_SEQ")
    @SequenceGenerator(
            name = "PKG_CBT_ID_SEQ",
            sequenceName = "PKG_CBT_ID_SEQ",
            allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "PACKAGE_ID", nullable = false)
    private Long packageId;

    @NotNull
    @Column(name = "CUSTOMER_ID", nullable = false)
    private Long customerId;

    @Column(name = "GIFT_PARTNER_ID", nullable = false)
    private Long giftPartnerId;


    @Column(name = "CIF_NO", length = 50)
    private String cifNo;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "STATUS", nullable = false, length = 10)
    private String status;

    @Column(name = "TYPE", nullable = false, length = 10)
    private String type;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder;

    @Column(name = "URL_IMAGE", length = 200)
    private String urlImage;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @NotNull
    @Column(name = "CREATED_BY", nullable = false)
    private Long createdBy;

    @NotNull
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDate createdAt;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_AT")
    private LocalDate updatedAt;

    @NotNull
    @Column(name = "IS_DELETED", nullable = false, columnDefinition = "CHAR(1)")
    private Boolean isDeleted = false;

}