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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "CUSTOMER_ID", nullable = false)
    private Long customer;

    @Column(name = "CHAIN_ID")
    private Integer chain;

    @Column(name = "MISSION_ID")
    private Integer mission;

    @Column(name = "PARENT_CHAIN_ID")
    private Integer parentChain;

    @Size(max = 20)
    @Column(name = "STATUS", length = 20)
    private String status;

    @Column(name = "GROUP_TYPE")
    private Boolean groupType;

    @Column(name = "ORDER_NO")
    private Short orderNo;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "COMPLETED_AT")
    private LocalDate completedAt;

}