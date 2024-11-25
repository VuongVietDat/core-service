package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.atomi.loyalty.core.entity.Partner;
import vn.com.atomi.loyalty.core.enums.Status;

import java.time.LocalDate;
import java.util.Optional;

public interface PartnerRepository extends JpaRepository<Partner, Long> {

    @Query(
            value =
                    "select r "
                            + "from Partner r "
                            + "where"
                            + " (:status is null or r.status = :status) "
                            + "order by r.updatedAt desc ")
    Page<Partner> findByCondition(Status status, Pageable pageable);
}
