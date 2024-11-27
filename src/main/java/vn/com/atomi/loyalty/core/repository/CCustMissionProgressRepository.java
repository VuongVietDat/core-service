package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.atomi.loyalty.core.entity.CCustMissionProgress;

import java.util.List;

public interface CCustMissionProgressRepository extends JpaRepository<CCustMissionProgress, Long> {

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
            " SELECT 1 FROM C_CUST_MISSION_PROGRESS cmp " +
            " WHERE ccm.ID = CMP.CHAIN_ID AND cmp.CUSTOMER_ID  = :customerId" +
            " AND cmp.STATUS = :status " +
            " AND ( CMP.MISSION_ID IS NOT NULL OR cmp.PARENT_CHAIN_ID IS NULL )) ", nativeQuery = true)
    List<Object[]> getRegistedChainMission(Long customerId, String status);

}