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
@Table(name = "PKG_GIFT_MAPPING")
public class PkgGiftMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PKG_GIFT_MAPPING_id_gen")
    @SequenceGenerator(name = "PKG_GIFT_MAPPING_id_gen", sequenceName = "PKG_GIFT_MAPPING_ID_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "PACKAGE_ID", nullable = false)
    private Long packageField;

    @Column(name = "PARTNER_ID")
    private Long partner;

    @Column(name = "GIFT_PARTNER_ID")
    private Long giftPartner;

    @Column(name = "GIFT_QUANTITY")
    private Integer giftQuantity;

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
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}