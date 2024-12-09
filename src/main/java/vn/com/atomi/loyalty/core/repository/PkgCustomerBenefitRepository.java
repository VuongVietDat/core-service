package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.atomi.loyalty.core.entity.PkgCustomerBenefit;

import java.util.List;

public interface PkgCustomerBenefitRepository extends JpaRepository<PkgCustomerBenefit, Long> {

    @Query(value = """
        SELECT
            to_char(pcb.ID),
            to_char(pcb.PACKAGE_ID),
            to_char(pcb.CUSTOMER_ID),
            to_char(pcb.GIFT_PARTNER_ID),
            DECODE(cp.IS_LOCAL, 'Y', 'B', 'N', 'P', NULL) TYPES,
            ggp.CODE,
            ggp.NAME,
            pcb.STATUS,
            pcb.GIFT_QUANTITY,
            ggp.IMAGE,
            ggp.NOTES,
            to_char(ggp.START_DATE,'dd/MM/yyyy'),
            to_char(ggp.END_DATE,'dd/MM/yyyy')
        FROM
            PKG_CUSTOMER_BENEFIT pcb
        JOIN GS_GIFT_PARTNER ggp ON
            pcb.GIFT_PARTNER_ID = ggp.ID
        JOIN C_PARTNER cp ON
            ggp.PARTNER_ID = cp.ID
        WHERE pcb.PACKAGE_ID = :packageId
        AND pcb.CIF_NO = :cifNo
        AND pcb.STATUS IN (:lstStatus)
        AND ggp.STATUS = 'ACTIVE'
        
    """, nativeQuery = true)
    List<Object[]> findByCondition(Long packageId, String cifNo,List<String> lstStatus);
}