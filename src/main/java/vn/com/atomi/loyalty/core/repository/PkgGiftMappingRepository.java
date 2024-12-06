package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.atomi.loyalty.core.entity.PkgCustomerBenefit;
import vn.com.atomi.loyalty.core.entity.PkgGiftMapping;

import java.util.List;

public interface PkgGiftMappingRepository extends JpaRepository<PkgGiftMapping, Long> {

    @Query(value = """
        SELECT gpg.ID, gpg.PACKAGE_ID, 
        DECODE(cp.IS_LOCAL,'Y','B','N','P',NULL) TYPES,
        ggp.CODE, ggp.NAME, gpg.GIFT_QUANTITY, ggp.IMAGE, ggp.NOTES
        FROM PKG_GIFT_MAPPING gpg
        JOIN GS_GIFT_PARTNER ggp ON gpg.GIFT_PARTNER_ID = ggp.ID
        JOIN C_PARTNER cp ON ggp.PARTNER_ID = cp.ID
        WHERE gpg.STATUS = 'ACTIVE'
        AND ggp.STATUS  = 'ACTIVE'
    """, nativeQuery = true)
    List<Object[]> getListGiftBenefits(Long packageId, String lstStatus);
}