package vn.com.atomi.loyalty.core.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.core.entity.CustomerGroup;
import vn.com.atomi.loyalty.core.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface CustomerGroupRepository extends JpaRepository<CustomerGroup, Long> {

  boolean existsByIdAndDeletedFalse(long id);

  Optional<CustomerGroup> findByDeletedFalseAndId(Long id);

  @Query(
      value =
          "select cg "
              + "from CustomerGroup cg "
              + "where cg.deleted = false "
              + "  and (:status is null or cg.status = :status) "
              + "  and (:name is null or cg.name like :name) "
              + "  and (:code is null or cg.code like :code) "
              + "order by cg.updatedAt desc ")
  Page<CustomerGroup> findByCondition(Status status, String name, String code, Pageable pageable);
}
