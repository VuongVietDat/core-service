package vn.com.atomi.loyalty.core.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.core.entity.CustomerGroupApproval;
import vn.com.atomi.loyalty.core.enums.ApprovalStatus;
import vn.com.atomi.loyalty.core.enums.ApprovalType;
import vn.com.atomi.loyalty.core.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface CustomerGroupApprovalRepository
    extends JpaRepository<CustomerGroupApproval, Long> {

  @Query(value = "select cf_customer_group_arv_id_seq.nextval from DUAL", nativeQuery = true)
  Long getSequence();

  Optional<CustomerGroupApproval> findByDeletedFalseAndId(Long id);

  @Query(
      "select cga from CustomerGroupApproval cga where cga.deleted = false "
          + "and cga.approvalStatus = vn.com.atomi.loyalty.core.enums.ApprovalStatus.ACCEPTED "
          + "and cga.customerGroupId = ?1 and cga.id < ?2 "
          + "order by cga.updatedAt desc "
          + "limit 1")
  Optional<CustomerGroupApproval> findLatestAcceptedRecord(Long ruleId, Long id);

  Optional<CustomerGroupApproval> findByDeletedFalseAndIdAndApprovalStatus(
      Long id, ApprovalStatus approvalStatus);

  @Query(
      value =
          "select cga "
              + "from CustomerGroupApproval cga "
              + "where cga.deleted = false "
              + "  and (:status is null or cga.status = :status) "
              + "  and (:approvalStatus is null or cga.approvalStatus = :approvalStatus)"
              + "  and (:approvalType is null or cga.approvalType = :approvalType)"
              + "  and (:name is null or cga.name like :name) "
              + "  and (:code is null or cga.code like :code) "
              + "  and (:startApprovedDate is null or (cga.updatedAt >= :startApprovedDate "
              + "       and cga.approvalStatus in (vn.com.atomi.loyalty.core.enums.ApprovalStatus.ACCEPTED, vn.com.atomi.loyalty.core.enums.ApprovalStatus.REJECTED))) "
              + "  and (:endApprovedDate is null or (cga.updatedAt >= :endApprovedDate "
              + "       and cga.approvalStatus in (vn.com.atomi.loyalty.core.enums.ApprovalStatus.ACCEPTED, vn.com.atomi.loyalty.core.enums.ApprovalStatus.REJECTED))) "
              + "order by cga.updatedAt desc ")
  Page<CustomerGroupApproval> findByCondition(
      Status status,
      ApprovalStatus approvalStatus,
      ApprovalType approvalType,
      String name,
      String code,
      LocalDateTime startApprovedDate,
      LocalDateTime endApprovedDate,
      Pageable pageable);
}
