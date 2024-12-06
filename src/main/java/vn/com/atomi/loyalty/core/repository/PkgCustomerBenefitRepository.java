package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.atomi.loyalty.core.entity.PkgCustomerBenefit;

import java.util.List;

public interface PkgCustomerBenefitRepository extends JpaRepository<PkgCustomerBenefit, Long> {

    @Query(value = "select pcb from PkgCustomerBenefit pcb " +
            " where pcb.packageId = :packageId " +
            " and pcb.cifNo = :cifNo " +
            " and :lstStatus is null or pcb.status in (:lstStatus)")
    List<PkgCustomerBenefit> findByCondition(Long packageId, String cifNo,List<String> lstStatus);
}