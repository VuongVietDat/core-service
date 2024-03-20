package vn.com.atomi.loyalty.core.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.core.entity.CustomerBalanceHistory;
import vn.com.atomi.loyalty.core.enums.ChangeType;
import vn.com.atomi.loyalty.core.enums.PointType;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface CustomerBalanceHistoryRepository
    extends JpaRepository<CustomerBalanceHistory, Long> {

  @Query(
      value =
          "select cb "
              + "from CustomerBalanceHistory cb "
              + "where cb.deleted = false "
              + "  and cb.customerId = :customerId "
              + "  and (:changeType is null or cb.changeType = :changeType) "
              + "  and (:pointType is null or cb.pointType = :pointType) "
              + "  and (:startTransactionDate is null or cb.searchTransactionDate >= :startTransactionDate) "
              + "  and (:endTransactionDate is null or cb.searchTransactionDate <= :endTransactionDate) "
              + "  and (:startExpiredDate is null or cb.expireAt >= :startExpiredDate) "
              + "  and (:endExpiredDate is null or cb.expireAt <= :endExpiredDate) ")
  Page<CustomerBalanceHistory> findHistory(
      Long customerId,
      ChangeType changeType,
      PointType pointType,
      String startTransactionDate,
      String endTransactionDate,
      LocalDate startExpiredDate,
      LocalDate endExpiredDate,
      Pageable pageable);

  @Query(
      value =
          "select cb "
              + "from CustomerBalanceHistory cb "
              + "where cb.deleted = false "
              + "  and cb.customerId = :customerId "
              + "  and cb.amount < cb.amountUsed "
              + "  and cb.expireAt is not null "
              + "order by cb.updatedAt desc "
              + "limit 1 ")
  Optional<CustomerBalanceHistory> findPointAboutExpire(Long customerId);
}
