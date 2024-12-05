package vn.com.atomi.loyalty.core.entity;

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

    @Column(name = "FEE")
    private Integer fee;

    @Column(name = "CURRENCY")
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

    @Column(name = "DESCRIPTION")
    private String description;
}