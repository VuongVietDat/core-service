package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.core.entity.Customer;
import vn.com.atomi.loyalty.core.entity.Packages;
import vn.com.atomi.loyalty.core.entity.PkgBenefit;
import vn.com.atomi.loyalty.core.entity.PkgPurchaseHistory;
import vn.com.atomi.loyalty.core.enums.Status;

import java.util.List;
import java.util.Optional;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface PkgPurchaseHistoryRepository extends JpaRepository<PkgPurchaseHistory, Long> {

    @Query(value = "select ps from PkgPurchaseHistory ps " +
            " where ps.cifNo = :cifNo " +
            " and (:packageId is null or ps.packageId <= :packageId )" +
            " and (ps.effectiveDate is null or ps.effectiveDate <= CURRENT_DATE )" +
            " and (ps.expiredDate is null or ps.expiredDate >= CURRENT_DATE) ")
    PkgPurchaseHistory getRegistedPackage(String cifNo, Long packageId);

}
