package vn.com.atomi.loyalty.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "C_CUST_MISSION_PROGRESS")
public class CCustMissionProgress {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "C_CUST_MISSION_PROGRESS_ID_SEQ")
    @SequenceGenerator(
            name = "C_CUST_MISSION_PROGRESS_ID_SEQ",
            sequenceName = "C_CUST_MISSION_PROGRESS_ID_SEQ",
            allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "CUSTOMER_ID", nullable = false)
    private Long customer;

    @Column(name = "CHAIN_ID")
    private Long chain;

    @Column(name = "MISSION_ID")
    private Long mission;

    @Column(name = "PARENT_CHAIN_ID")
    private Long parentChain;

    @Size(max = 10)
    @Column(name = "STATUS", length = 10)
    private String status;

    @Column(name = "GROUP_TYPE")
    private Boolean groupType;

    @Column(name = "ORDER_NO")
    private Integer orderNo;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "COMPLETED_AT")
    private LocalDate completedAt;

}