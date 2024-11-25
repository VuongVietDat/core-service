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

  @Query(value = " SELECT ccm FROM CChainMission ccm " +
          " WHERE (ccm.isDeleted IS NULL OR ccm.isDeleted = false) " +
          " AND ccm.isChained = :isChain " +
          " AND ccm.status = :status" +
          " AND EXISTS ( " +
          " SELECT cmp.id FROM CCustMissionProgress cmp " +
          " WHERE ccm.id = cmp.chain AND cmp.customer = :customerId " +
          " AND ( cmp.mission IS NOT NULL OR cmp.parentChain IS NULL )) ")
  List<CChainMission> getChainMissionProgress1(Chain isChain,Status status, Long customerId);

  @Query(value = " SELECT ccm.id, " +
          "               ccm.code, " +
          "               ccm.name, " +
          "               ccm.group_type, " +
          "               ccm.benefit_type, " +
          "               ccm.image, " +
          "               ccm.is_ordered, " +
          "               ccm.price, " +
          "               ccm.currency, " +
          "               ccm.notes, " +
          "               ccm.start_date, " +
          "               ccm.end_date" +
          " FROM C_CHAIN_MISSION ccm " +
          " WHERE (IS_DELETED IS NULL OR IS_DELETED != 'Y') " +
          " AND IS_CHAINED = 'Y' " +
          " AND STATUS = 'ACTIVE'" +
          " AND EXISTS (" +
          " SELECT 1 FROM C_CUST_MISSION_PROGRESS CMP " +
          " WHERE ccm.ID = CMP.CHAIN_ID AND CMP.CUSTOMER_ID  = :customerId " +
          " AND ( CMP.MISSION_ID IS NOT NULL OR CMP.PARENT_CHAIN_ID IS NULL )) ", nativeQuery = true)
  List<Object[]> getChainMissionProgress(Long customerId);
}
