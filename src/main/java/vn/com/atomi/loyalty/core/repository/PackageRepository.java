package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.core.entity.Packages;
import vn.com.atomi.loyalty.core.enums.RefType;
import vn.com.atomi.loyalty.core.enums.Status;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface PackageRepository extends JpaRepository<Packages, Long> {
  @Query(value = "select PACKAGES_ID_SEQ.nextval from DUAL", nativeQuery = true)
  Long getSequence();

  @Query(value = "select ps " +
          " from Packages ps " +
//          " join fetch ps.benefits " +
          " where ps.status = :status")
  List<Packages> getListPackage(Status status);
  @Query(value = "select ps from Packages ps " +
//          " join fetch ps.benefits " +
          " where ps.status = :status " +
          " and ps.id in (" +
          "   select tel.refId " +
          "   from TransExternal tel " +
          "   where tel.cifNo = :cifNo " +
          "   and tel.refType = :refType" +
          " ) ")
  Packages getRegistedPackage(Status status, String cifNo, RefType refType);
}
