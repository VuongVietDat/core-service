package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.atomi.loyalty.core.entity.PkgCustomerBenefit;
import vn.com.atomi.loyalty.core.entity.PkgGiftMapping;

import java.util.List;

public interface PkgGiftMappingRepository extends JpaRepository<PkgGiftMapping, Long> {

    @Query(value = """
    """, nativeQuery = true)
    List<PkgGiftMapping> getListGiftBenefit(Long packageId, String cifNo, List<String> lstStatus);
}