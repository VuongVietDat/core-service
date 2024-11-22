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

  @Query(value = "SELECT ccms FROM CChainMission ccms WHERE ccms.isDeleted = false " +
          " and ccms.isChained = :isChained AND ccms.status = :status ")
  List<CChainMission> getNewChainMission(Chain isChained, Status status);

  @Query(value = " SELECT * FROM C_CHAIN_MISSION ccm " +
          " WHERE (IS_DELETED IS NULL OR IS_DELETED != 'Y') " +
          " AND IS_CHAINED = 'Y' " +
          " AND STATUS = 'ACTIVE'" +
          " AND EXISTS (" +
          " SELECT 1 FROM C_CUST_MISSION_PROGRESS CMP " +
          " WHERE ccm.ID = CMP.CHAIN_ID AND CMP.CUSTOMER_ID  = :customerId " +
          " AND ( CMP.MISSION_ID IS NOT NULL OR CMP.PARENT_CHAIN_ID IS NULL )) ", nativeQuery = true)
  List<CChainMission> getChainMissionProgress(Long customerId);
}
