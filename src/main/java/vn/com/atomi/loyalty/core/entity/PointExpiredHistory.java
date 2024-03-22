package vn.com.atomi.loyalty.core.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "C_POINT_EXPIRED_HISTORY")
public class PointExpiredHistory {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "C_POINT_EXPIRED_HISTORY_ID_SEQ")
  @SequenceGenerator(
      name = "C_POINT_EXPIRED_HISTORY_ID_SEQ",
      sequenceName = "C_POINT_EXPIRED_HISTORY_ID_SEQ",
      allocationSize = 1)
  private long id;

  @Column(name = "END_AT")
  private LocalDateTime endAt;

  @Column(name = "EXPIRED_AT")
  private LocalDate expiredAt;

  @Column(name = "REF_NO")
  private String refNo;

  @Column(name = "START_AT")
  private LocalDateTime startAt;

  @Column(name = "TOTAL_POINT_EXPIRED")
  private Long totalPointExpired;
}
