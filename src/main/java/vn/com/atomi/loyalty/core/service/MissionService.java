package vn.com.atomi.loyalty.core.service;

import vn.com.atomi.loyalty.core.dto.output.CChainMissionOuput;
import vn.com.atomi.loyalty.core.dto.output.CMissionOuput;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
public interface MissionService {
  List<CChainMissionOuput> getNewChainMission();
  List<CChainMissionOuput> getMissionInProgress(String cifNo);
  List<CMissionOuput> getListMission(Long chainId);
  CMissionOuput getMissionDetail(Integer id);;
}
