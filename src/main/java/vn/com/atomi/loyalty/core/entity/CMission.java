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

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "C_MISSION")
public class CMission {
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

    @NotNull
    @Column(name = "STATUS", nullable = false)
    private String status;

    @NotNull
    @Column(name = "BENEFIT_TYPE", nullable = false)
    private String benefitType;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "PRICE")
    private Long price;

    @Size(max = 3)
    @Column(name = "CURRENCY", length = 3)
    private String currency;

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