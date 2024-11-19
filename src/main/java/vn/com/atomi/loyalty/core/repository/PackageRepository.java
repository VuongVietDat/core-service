package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.core.dto.output.GetListPackageOutput;
import vn.com.atomi.loyalty.core.dto.projection.CustomerPointAccountProjection;
import vn.com.atomi.loyalty.core.entity.Packages;
import vn.com.atomi.loyalty.core.enums.Status;

import java.util.List;
import java.util.Optional;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface PackageRepository extends JpaRepository<Packages, Long> {
  @Query(value = "select PACKAGES_ID_SEQ.nextval from DUAL", nativeQuery = true)
  Long getSequence();

  @Query(value = "select ps from Packages ps where ps.status = :status")
  List<Packages> getListPackage(Status status);
  @Query(value = "select ps from Packages ps where ps.id = :packageId and ps.status = :status")
  Packages getRegistedPackage(String packageId, Status status);
}
