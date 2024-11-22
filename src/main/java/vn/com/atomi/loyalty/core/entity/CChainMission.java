package vn.com.atomi.loyalty.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import vn.com.atomi.loyalty.core.enums.Chain;
import vn.com.atomi.loyalty.core.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "C_CHAIN_MISSION")
public class CChainMission {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotNull
    @Column(name = "CODE", nullable = false, length = 30)
    private String code;

    @Size(max = 200)
    @NotNull
    @Nationalized
    @Column(name = "NAME", nullable = false, length = 200)
    private String name;

    @Size(max = 10)
    @Nationalized
    @Column(name = "STATUS", length = 10)
    private Status status;

    @Size(max = 10)
    @Column(name = "BENEFIT_TYPE", length = 10)
    private String benefitType;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "PRICE", precision = 12, scale = 2)
    private BigDecimal price;

    @Size(max = 10)
    @Column(name = "CURRENCY", length = 10)
    private String currency;

    @Size(max = 10)
    @Column(name = "IS_CHAINED", length = 10)
    private Chain isChained;

    @Size(max = 10)
    @Column(name = "IS_ORDERED", length = 10)
    private String isOrdered;

    @Size(max = 10)
    @Column(name = "GROUP_TYPE", length = 10)
    private String groupType;

    @Size(max = 100)
    @Nationalized
    @Column(name = "IMAGE", length = 100)
    private String image;

    @Size(max = 2000)
    @Nationalized
    @Column(name = "NOTES", length = 2000)
    private String notes;

    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @Size(max = 20)
    @Column(name = "CREATED_BY", length = 20)
    private String createdBy;

    @Column(name = "CREATED_AT")
    private LocalDate createdAt;

    @Size(max = 20)
    @Column(name = "UPDATED_BY", length = 20)
    private String updatedBy;

    @Column(name = "UPDATED_AT")
    private LocalDate updatedAt;

}