package vn.com.atomi.loyalty.core.entity;

import jakarta.persistence.*;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "CUST_ID", nullable = false)
    private Long custId;

    @NotNull
    @Column(name = "CHAIN_ID", nullable = false)
    private Integer chainId;

    @NotNull
    @Column(name = "REG_DATE", nullable = false)
    private LocalDate regDate;


}