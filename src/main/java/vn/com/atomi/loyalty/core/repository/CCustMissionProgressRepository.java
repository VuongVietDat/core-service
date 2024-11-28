package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.atomi.loyalty.core.entity.CCustMissionProgress;
import vn.com.atomi.loyalty.core.entity.CMissionSequential;

import java.util.List;

public interface CCustMissionProgressRepository extends JpaRepository<CCustMissionProgress, Long> {

    @Query(value= """
        SELECT cmps FROM CCustMissionProgress cmps 
        WHERE cmps.customer = :customerId 
        AND cmps.missionId = :chainId 
        AND cmps.missionType = :missionType
        AND cmps.status = :status
    """)
    CCustMissionProgress findByCustomerAndChainId(Long customerId, Long chainId, String missionType, String status);

    @Query(value = """
            SELECT
                cms2.ID, cms2.CUSTOMER_ID, cms2.PARENT_ID, cms2.MISSION_ID,
                cms2.MISSION_TYPE, cms2.ORDER_NO, cms2.GROUP_TYPE,
                (CASE WHEN cms2.MISSION_TYPE IN ('C','G') THEN ccmns.START_DATE ELSE cmns.START_DATE END) START_DATE,
                (CASE WHEN cms2.MISSION_TYPE IN ('C','G') THEN ccmns.END_DATE ELSE cmns.END_DATE END) END_DATE,
                'PENDING' STATUS, :refNo as TXN_REF_NO, NULL as COMPLETED_AT
            FROM (
                SELECT
                    ID,
                    (SELECT ID FROM C_CUSTOMER cc WHERE CIF_BANK = :cifBank) CUSTOMER_ID,
                    PARENT_ID, MISSION_ID, MISSION_TYPE,
                    NVL(ORDER_NO, PRIOR ORDER_NO) ORDER_NO,
                    (
                        SELECT ccms.GROUP_TYPE
                        FROM C_CHAIN_MISSION ccms
                        WHERE ccms.ID = cms.PARENT_ID
                    ) GROUP_TYPE
                FROM   C_MISSION_SEQUENTIAL cms
                WHERE (IS_DELETED IS NULL OR IS_DELETED = 'N')
                CONNECT BY PRIOR MISSION_ID = PARENT_ID
                START WITH MISSION_ID = :chainId
                ORDER BY MISSION_TYPE,ORDER_NO
            ) cms2
            LEFT JOIN C_CHAIN_MISSION ccmns ON cms2.MISSION_TYPE IN ('C','G') AND cms2.MISSION_ID = ccmns.ID AND ccmns.STATUS = 'ACTIVE'
            LEFT JOIN C_MISSION cmns ON cms2.MISSION_TYPE = 'M' AND cms2.MISSION_ID = cmns.ID AND cmns.STATUS = 'ACTIVE'
    """, nativeQuery = true)
    List<CCustMissionProgress> getDataChainMission(String refNo, String cifBank, Long chainId);

}