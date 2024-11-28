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
                            + "and (:keyword is null or lower(r.code) like lower('%' || :keyword || '%')) "
                            + "and (:keyword is null or lower(r.name) like lower('%' || :keyword || '%')) "
                            + "and (:startDate is null or r.startDate >= :startDate)"
                            + "order by r.updatedAt desc ")
    Page<Partner> findByCondition(Status status, String keyword, LocalDate startDate, Pageable pageable);

    Optional<Partner> findByDeletedFalseAndCode(String code);

    Optional<Partner> findByDeletedFalseAndId(Long id);
}
