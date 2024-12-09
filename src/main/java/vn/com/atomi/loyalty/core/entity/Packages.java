package vn.com.atomi.loyalty.core.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Nationalized;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.core.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PACKAGES")
public class Packages {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PACKAGES_ID_SEQ")
    @SequenceGenerator(
            name = "PACKAGES_ID_SEQ",
            sequenceName = "PACKAGES_ID_SEQ",
            allocationSize = 1)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE")
    private String code;

    @Column(name = "DURATION")
    private Integer duration;

    @Column(name = "FEE")
    private Integer fee;

    @Column(name = "CURRENCY", columnDefinition = "CHAR(3)")
    private String currency;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "URL_IMAGE")
    private String urlImage;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder;

    @Column(name = "EFFECTIVE_DATE")
    private LocalDate effectiveDate;

    @Column(name = "EXPRIRED_DATE")
    private LocalDate expriredDate;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_AT")
    private LocalDate createdAt;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_AT")
    private LocalDate updatedAt;

    @Column(name = "APPROVAL_BY")
    private Integer approvalBy;

    @Column(name = "APPROVAL_AT")
    private LocalDate approvalAt;

    @Column(name = "APPROVAL_STATUS", columnDefinition = "CHAR(1)")
    private String approvalStatus;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "packages", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PkgBenefit> pkgBenefit = new ArrayList<>();
}