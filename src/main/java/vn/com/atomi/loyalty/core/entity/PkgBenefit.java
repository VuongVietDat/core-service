package vn.com.atomi.loyalty.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PKG_BENEFIT")
public class PkgBenefit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PKG_BENEFIT_id_gen")
    @SequenceGenerator(name = "PKG_BENEFIT_id_gen", sequenceName = "PKG_BENEFIT_ID_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PACKAGE_ID", nullable = false)
    private Packages packageField;

    @NotNull
    @Column(name = "PRODUCT_LINE_ID", nullable = false)
    private Long productLine;

    @NotNull
    @Column(name = "PRODUCT_ID", nullable = false)
    private Long product;

    @Column(name = "TXN_REFUND")
    private Float txnRefund;

    @Column(name = "TXN_MAX_AMOUNT")
    private Long txnMaxAmount;

    @Column(name = "CUST_MAX_AMOUNT")
    private Long custMaxAmount;

    @Column(name = "CUST_REFUND_TIME")
    private String custRefundTime;

    @Size(max = 200)
    @Nationalized
    @Column(name = "URL_IMAGE", length = 200)
    private String urlImage;

    @Size(max = 500)
    @Nationalized
    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Size(max = 10)
    @NotNull
    @Nationalized
    @Column(name = "STATUS", nullable = false, length = 10)
    private String status;

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