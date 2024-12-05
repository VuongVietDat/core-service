package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.atomi.loyalty.core.entity.TransExternal;
import vn.com.atomi.loyalty.core.enums.RefType;

public interface TransExternalRepository extends JpaRepository<TransExternal, String> {
    @Query(value = "select 1 from TransExternal tel " +
            " where tel.cifNo = :cifNo " +
            " and (:refId is null or tel.refId <= :refId )" +
            " and (:refType is null or tel.refType <= :refType )" +
            " and (tel.effectiveDate is null or tel.effectiveDate <= CURRENT_DATE )" +
            " and (tel.expiredDate is null or tel.expiredDate >= CURRENT_DATE) ")
    Integer findTransExternalByCondition(String cifNo, Long refId, RefType refType);
}