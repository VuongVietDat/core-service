package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.core.entity.PkgBenefit;
import vn.com.atomi.loyalty.core.enums.Status;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface PkgBenefitRepository extends JpaRepository<PkgBenefit, Long> {
  @Query(value = "select PACKAGES_ID_SEQ.nextval from DUAL", nativeQuery = true)
  Long getSequence();

  @Query(value = "select ps from PkgBenefit ps " +
          " where ps.packageId = :packageId " +
          " and ps.status = :status")
  List<PkgBenefit> getListBenefit(Long packageId, Status status);

  @Query(value = "select ps from PkgBenefit ps " +
          " where ps.packageId = :packageId " +
          " and ps.status = :status")
  Page<PkgBenefit> getPageBenefit(Long packageId, Status status, Pageable pageable);
}
