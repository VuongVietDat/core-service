package vn.com.atomi.loyalty.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;

import java.util.Date;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "C_LOGINPERIOD")
public class LoginPeriod extends BaseEntity {

    @Id
    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "CURRENT_STREAK", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer currentStreak;

    @Column(name = "LAST_LOGIN_DATE")
    private Date lastLoginDate;

    @Column(name = "STREAK_RESET_DATE")
    private Date streakResetDate;

    @Column(name = "MONTH_PLUS_POINT")
    private Date monthPlusPoint;
}
