package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.core.entity.CustomerBalanceHistory;
import vn.com.atomi.loyalty.core.enums.ChangeType;

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
              + "  and cb.pointType = vn.com.atomi.loyalty.core.enums.PointType.CONSUMPTION_POINT "
              + "  and (:changeType is null or cb.changeType = :changeType) "
              + "  and (:searchDate is null or (cb.searchTransactionDate >= :startDate and cb.searchTransactionDate <= :endDate)) ")
  Page<CustomerBalanceHistory> findHistory(
      Long customerId,
      ChangeType changeType,
      String searchDate,
      String startDate,
      String endDate,
      Pageable pageable);
}
