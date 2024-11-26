package vn.com.atomi.loyalty.core.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.core.enums.PaymentType;

import java.time.LocalDate;
import java.util.Date;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "C_COUNTPERIOD")
public class CountPeriod extends BaseEntity {

    @Id
    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "CURRENT_STREAK", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer currentStreak;

    @Column(name = "LAST_PAYMENT_DATE")
    private Date lastPaymentDate;

    @Column(name = "STREAK_RESET_DATE")
    private Date streakResetDate;

    @Column(name = "PAYMENT_TYPE")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
}
