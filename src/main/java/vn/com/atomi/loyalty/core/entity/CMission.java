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

    @NotNull
    @Column(name = "CODE", nullable = false)
    private String code;

    @NotNull
    @Column(name = "NAME", nullable = false)
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

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "IMAGE")
    private String image;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_AT")
    private LocalDate createdAt;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "UPDATED_AT")
    private LocalDate updatedAt;

}