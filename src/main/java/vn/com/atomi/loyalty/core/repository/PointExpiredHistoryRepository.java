package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.core.entity.PointExpiredHistory;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface PointExpiredHistoryRepository extends JpaRepository<PointExpiredHistory, Long> {}
