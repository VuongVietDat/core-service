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
                SELECT cms2.PARENT_ID, cmns.ID, to_char(cms2.ORDER_NO),
                cms2.GROUP_TYPE,
                cmns.CODE, cmns.NAME, cmns.BENEFIT_TYPE,
                cmns.START_DATE, cmns.END_DATE, cmns.PRICE,
                cmns.CURRENCY, cmns.IMAGE,cmns.NOTES
                FROM (
                    SELECT PARENT_ID, MISSION_ID, NVL (ORDER_NO, PRIOR ORDER_NO) ORDER_NO, MISSION_TYPE,
                    (
                        SELECT DECODE (ccms.GROUP_TYPE, '0', 'OR', '1', 'AND', '?')
                        FROM C_CHAIN_MISSION ccms
                        WHERE ccms.ID = cms.PARENT_ID
                    ) GROUP_TYPE
                    FROM   C_MISSION_SEQUENTIAL cms
                    WHERE (IS_DELETED IS NULL OR IS_DELETED = '0') AND MISSION_TYPE = 'M'
                    CONNECT BY PRIOR MISSION_ID = PARENT_ID
                    START WITH MISSION_ID = :chainId
                ) cms2
                JOIN C_MISSION cmns ON cms2.MISSION_TYPE = 'M'
                AND cms2.MISSION_ID = cmns.ID AND cmns.STATUS = 'ACTIVE'""", nativeQuery = true)
    List<Object[]> getListMission(Long chainId);

    @Query( value = "SELECT cmn FROM CMission cmn " +
            " WHERE (cmn.isDeleted is null or cmn.isDeleted = false) and cmn.id = :id " +
            " AND cmn.status = :status")
    CMission getMissionDetail(Long id, Status status);
}