package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.atomi.loyalty.core.entity.CChainMission;
import vn.com.atomi.loyalty.core.entity.CMission;
import vn.com.atomi.loyalty.core.enums.Status;

import java.util.List;

public interface CMissionRepository extends JpaRepository<CMission, Long> {

    @Query(
            value = """
                   SELECT cms2.CHAIN_ID, cmns.ID, to_char(cms2.ORDER_NO), 
                   DECODE (cmn.GROUP_TYPE, '0', 'OR', '1', 'AND', '?') GROUP_TYPE,
                   cmns.CODE, cmns.NAME, cmns.BENEFIT_TYPE,
                   cmns.START_DATE, cmns.END_DATE, cmns.PRICE,
                   cmns.CURRENCY, cmns.IMAGE,cmns.NOTES
                   FROM (
                       SELECT CHAIN_ID, MISSION_ID, NVL (ORDER_NO, PRIOR ORDER_NO) ORDER_NO
                       FROM   C_MISSION_SEQUENTIAL cms
                       WHERE IS_DELETED IS NULL OR IS_DELETED != 'N'
                       CONNECT BY PRIOR PR_chain_id = CHAIN_ID
                       START WITH CHAIN_ID = :chainId
                        ) cms2, C_CHAIN_MISSION cmn, C_MISSION cmns
                   WHERE cms2.MISSION_ID IS NOT NULL AND cms2.CHAIN_ID = cmn.ID AND cmn.STATUS = 'ACTIVE'
                   AND cmns.ID = cms2.MISSION_ID AND cmns.STATUS = 'ACTIVE'
                   ORDER BY cms2.ORDER_NO, cms2.MISSION_ID""", nativeQuery = true)
    List<Object[]> getListMission(Long chainId);

    @Query( value = "SELECT cmn FROM CMission cmn " +
            " WHERE (cmn.isDeleted is null or cmn.isDeleted = false) and cmn.id = :id " +
            " AND cmn.status = :status")
    CMission getMissionDetail(Long id, Status status);
}