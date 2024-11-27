package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.core.entity.CChainMission;
import vn.com.atomi.loyalty.core.enums.Chain;
import vn.com.atomi.loyalty.core.enums.Status;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface ChainMissionRepository extends JpaRepository<CChainMission, Long> {
//  @Query(value = "select PACKAGES_ID_SEQ.nextval from DUAL", nativeQuery = true)
//  Long getSequence();

  @Query(value = "SELECT ccms FROM CChainMission ccms WHERE (ccms.isDeleted is null or ccms.isDeleted = false) " +
          " and ccms.isChained = :isChained AND ccms.status = :status ")
  List<CChainMission> getNewChainMission(Chain isChained, Status status);
  @Query(value = "SELECT ccms FROM CChainMission ccms WHERE (ccms.isDeleted is null or ccms.isDeleted = false) " +
          " and ccms.id = :id AND ccms.status = :status ")
  CChainMission getChainMissionDetail(Long id, Status status);

  @Query(value = """
          SELECT ccm.id,
             ccm.code,
             ccm.name,
             ccm.group_type,
             ccm.benefit_type,
             ccm.image,
             ccm.is_ordered,
             ccm.price,
             ccm.currency,
             ccm.notes,
             ccm.start_date,
             ccm.end_date
          FROM C_CHAIN_MISSION ccm
          WHERE (IS_DELETED IS NULL OR IS_DELETED = 0)
          AND IS_CHAINED = 'Y'
          AND STATUS = 'ACTIVE'
          AND EXISTS (
              SELECT 1 FROM C_CUST_MISSION_PROGRESS cmp
              WHERE ccm.ID = cmp.MISSION_ID AND cmp.CUSTOMER_ID  = :customerId
              AND MISSION_TYPE IN ('C','G')
          )
""", nativeQuery = true)
  List<Object[]> getRegistedChainMission(Long customerId);
}
