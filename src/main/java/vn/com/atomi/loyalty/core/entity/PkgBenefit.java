package vn.com.atomi.loyalty.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.atomi.loyalty.core.enums.Status;

import java.sql.Date;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PKG_BENEFIT")
public class PkgBenefit {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PKG_BENEFIT_ID_SEQ")
    @SequenceGenerator(
            name = "PKG_BENEFIT_ID_SEQ",
            sequenceName = "PKG_BENEFIT_ID_SEQ",
            allocationSize = 1)
    private Long id;

    @Column(name = "PACKAGE_ID")
    private Long packageId;

    @Column(name = "NAME",unique = true)
    private String name;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder;

    @Column(name = "URL_IMGAGE")
    private String urlImgage;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_AT")
    private Date updatedAt;

}
