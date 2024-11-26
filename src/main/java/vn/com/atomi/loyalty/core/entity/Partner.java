package vn.com.atomi.loyalty.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.core.enums.Status;

import java.time.LocalDate;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "C_PARTNER")
public class Partner extends BaseEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "c_partner_id_seq")
    @SequenceGenerator(name = "c_partner_id_seq", sequenceName = "c_partner_id_seq", allocationSize = 1)
    private Long id;

    @Size(max = 20)
    @NotNull
    @Column(name = "CODE", nullable = false, length = 20)
    private String code;

    @Size(max = 200)
    @Nationalized
    @Column(name = "NAME", length = 200)
    private String name;

    @Size(max = 200)
    @Nationalized
    @Column(name = "NAME_EN", length = 200)
    private String nameEn;

    @Column(name = "TAX_CODE")
    private Integer taxCode;

    @Size(max = 20)
    @Column(name = "PHONE", length = 20)
    private String phone;

    @Size(max = 20)
    @Column(name = "EMAIL", length = 20)
    private String email;

    @Size(max = 200)
    @Nationalized
    @Column(name = "ADDRESS", length = 200)
    private String address;

    @Size(max = 200)
    @Nationalized
    @Column(name = "REPRESENSATIVE", length = 200)
    private String represensative;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Size(max = 255)
    @Column(name = "IS_LOCAL")
    private String isLocal;

}
