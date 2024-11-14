package vn.com.atomi.loyalty.core.entity;

import jakarta.persistence.*;
import lombok.*;
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

    @Column(name = "PARTNER_NAME", unique = true)
    private String name;

    @Column(name = "PARTNER_NAME_ENG", unique = true)
    private String nameEng;

    @Column(name = "PARTNER_CODE", unique = true)
    private String code;

    @Column(name = "TAX_CODE", unique = true)
    private Integer taxCode;

    @Column(name = "ADDRESS", unique = true)
    private String address;

    @Column(name = "AGENT", unique = true)
    private String agent;

    @Column(name = "PHONE_NUMBER", unique = true)
    private String phone;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "START_DATE", unique = true)
    private LocalDate startDate;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;
}
