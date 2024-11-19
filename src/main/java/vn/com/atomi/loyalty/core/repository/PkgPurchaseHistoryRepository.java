package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.core.entity.Packages;
import vn.com.atomi.loyalty.core.entity.PkgBenefit;
import vn.com.atomi.loyalty.core.entity.PkgPurchaseHistory;
import vn.com.atomi.loyalty.core.enums.Status;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface PkgPurchaseHistoryRepository extends JpaRepository<PkgPurchaseHistory, Long> {

    @Query(value = "select ps from Packages ps where ps.id = :packageId and ps.status = :status")
    Packages getRegistedPackage(String packageId, Status status);
}
