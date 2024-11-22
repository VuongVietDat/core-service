package vn.com.atomi.loyalty.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.core.dto.output.CChainMissionOuput;
import vn.com.atomi.loyalty.core.enums.Chain;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.repository.*;
import vn.com.atomi.loyalty.core.service.MissionService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class MissionServiceImpl extends BaseService implements MissionService {

  private final ChainMissionRepository chainMissionRepository;

  private final CustomerRepository customerRepository;

  private final CCustMissionProgressRepository cCustMissionProgressRepository;


  @Override
  public List<CChainMissionOuput> getNewChainMission() {
    var chainMission = chainMissionRepository.getNewChainMission(Chain.Y, Status.ACTIVE);
    return super.modelMapper.convertChainMissionOutput(chainMission);
  }
  @Override
  public List<CChainMissionOuput> getMissionInProgress(String cifNo) {
    var customer = customerRepository.findByParams(cifNo,
            Status.ACTIVE, PageRequest.of(0, 1));
    if(customer.getContent() != null) {
      var CChainMission = chainMissionRepository.getChainMissionProgress(customer.getContent().get(0).getId());
      return super.modelMapper.convertChainMissionOutput(CChainMission);
    }
    return new ArrayList<>();
  }
}
