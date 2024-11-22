package vn.com.atomi.loyalty.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.atomi.loyalty.core.entity.CChainMission;
import vn.com.atomi.loyalty.core.entity.CCustMissionProgress;
import vn.com.atomi.loyalty.core.enums.Chain;
import vn.com.atomi.loyalty.core.enums.Status;

import java.util.List;

public interface CCustMissionProgressRepository extends JpaRepository<CCustMissionProgress, Long> {

}