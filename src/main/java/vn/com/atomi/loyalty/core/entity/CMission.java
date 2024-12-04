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
@Table(name = "C_MISSION")
public class CMission {
    // dung chung sequence bang chain_mission tranh bi trung sequence
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "C_CHAIN_MISSION_ID_SEQ")
    @SequenceGenerator(
            name = "C_CHAIN_MISSION_ID_SEQ",
            sequenceName = "C_CHAIN_MISSION_ID_SEQ",
            allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "CODE", nullable = false, length = 30)
    private String code;

    @NotNull
    @Column(name = "NAME", nullable = false, length = 200)
    private String name;

    @NotNull
    @Column(name = "STATUS", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    @Column(name = "BENEFIT_TYPE", nullable = false, length = 1)
    private String benefitType;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "PRICE")
    private Long price;

    @Column(name = "CURRENCY", length = 3)
    private String currency;

    @Column(name = "IMAGE", length = 200)
    private String image;

    @Column(name = "NOTES", length = 2000)
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