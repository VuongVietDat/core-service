package vn.com.atomi.loyalty.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "C_CUST_MISSION")
public class CCustMission {
    @NotNull
    @Column(name = "CUST_ID", nullable = false)
    private Long custId;

    @NotNull
    @Column(name = "CHAIN_ID", nullable = false)
    private Integer chainId;

    @NotNull
    @Column(name = "REG_DATE", nullable = false)
    private LocalDate regDate;

    @Column(name = "ID")
    private Long id;

}