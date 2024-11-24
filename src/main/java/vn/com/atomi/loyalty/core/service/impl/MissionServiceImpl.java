package vn.com.atomi.loyalty.core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.core.dto.output.CChainMissionOuput;
import vn.com.atomi.loyalty.core.dto.output.CMissionOuput;
import vn.com.atomi.loyalty.core.entity.CMission;
import vn.com.atomi.loyalty.core.enums.Chain;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.repository.*;
import vn.com.atomi.loyalty.core.service.MissionService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haidv
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class MissionServiceImpl extends BaseService implements MissionService {

  private final ChainMissionRepository chainMissionRepository;

  private final CustomerRepository customerRepository;

  private final CMissionRepository missionRepository;

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
      var chainMission = chainMissionRepository.getChainMissionProgress(customer.getContent().get(0).getId());
      return this.mappingMissionProgress(chainMission);
    }
    return new ArrayList<>();
  }
  @Override
  public List<CMissionOuput> getListMission(Long chainId) {
      List<CMissionOuput> response = new ArrayList<>();
    var lstMission = missionRepository.getListMission(chainId);
      if (!lstMission.isEmpty()) {
          response = this.mappingListMission(lstMission);
      }
    return response;
  }
  @Override
  public CMissionOuput getMissionDetail(Integer id) {
    var mission = missionRepository.findById(id);
    return modelMapper.convertMissionDetailOutput(mission);
  }

  private List<CChainMissionOuput> mappingMissionProgress (List<Object[]> rawData){
      return rawData.stream()
          .map(data -> {
              CChainMissionOuput output = new CChainMissionOuput();

                output.setId((Integer) data[0]);  // Assuming column 0 is chainId
                output.setCode((String) data[1]);  // Assuming column 1 is missionId
                output.setName((String) data[2]);  // Assuming column 2 is orderNo
                output.setGroupType(data.length > 3 ? (String) data[3] : null);  // Assuming column 3 is groupType
                output.setBenefitType(data.length > 4 ? (String) data[4] : null);  // Assuming column 3 is groupType
                output.setImage(data.length > 5 ? (String) data[5] : null);  // Assuming column 4 is code
                output.setIsOrdered(data.length > 6 ? (String) data[6] : null);  // Assuming column 5 is name
                output.setPrice(data.length > 7 ? (BigDecimal) data[7 ] : null);  // Assuming column 6 is price
                output.setCurrency(data.length > 8 ? (String) data[8] : null);  // Assuming column 7 is currency
                output.setNotes(data.length > 9 ? (String) data[9] : null);  // Assuming column 8 is currency
                if (data.length > 10 && data[10] != null) {
                    Timestamp startDate = (Timestamp) data[10];
                    output.setStartDate(new SimpleDateFormat(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE).format(startDate.getTime()));
                }
                if (data.length > 11 && data[11] != null) {
                    Timestamp endDate = (Timestamp) data[11];
                    output.setEndDate(new SimpleDateFormat(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE).format(endDate.getTime()));
                }
                return output;

          }).collect(Collectors.toList());
  }
  private List<CMissionOuput> mappingListMission (List<Object[]> rawData){
      return rawData.stream()
          .map(data -> {
                CMissionOuput output = new CMissionOuput();

                output.setChainId((Integer) data[0]);  // Assuming column 0 is chainId
                output.setId((Integer) data[1]);  // Assuming column 1 is missionId
                output.setOrderNo(Integer.parseInt((String) data[2]));  // Assuming column 2 is orderNo
                output.setGroupType((String) data[3]);  // Assuming column 3 is groupType
                output.setCode(data.length > 4 ? (String) data[4] : null);  // Assuming column 4 is code
                output.setName(data.length > 5 ? (String) data[5] : null);  // Assuming column 5 is name
                output.setBenefitType(data.length > 6 ? String.valueOf((Character) data[6]) : null);  // Assuming column 6 is benefitType
                if (data.length > 7 && data[7] != null) {
                    Timestamp startDate = (Timestamp) data[7];
                    output.setStartDate(new SimpleDateFormat(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE).format(startDate.getTime()));
                }
                if (data.length > 8 && data[8] != null) {
                    Timestamp endDate = (Timestamp) data[8];
                    output.setEndDate(new SimpleDateFormat(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE).format(endDate.getTime()));
                }
                output.setPrice(data.length > 9? (BigDecimal) data[9] : null);  // Assuming column 9 is price
                output.setCurrency(data.length > 10 ? (String) data[10] : null);  // Assuming column 10 is currency
                output.setImage(data.length > 11 ? (String) data[11] : null);  // Assuming column 11 is image
                output.setNotes(data.length > 12 ? (String) data[12] : null);  // Assuming column 12 is notes
                return output;

          }).collect(Collectors.toList());
  }
  private CMissionOuput mappingMission (CMission data){
        CMissionOuput output = new CMissionOuput();
        try {
            output.setId(data.getId());
            output.setCode(data.getCode());
            output.setName(data.getName());
            output.setBenefitType(data.getBenefitType());
            output.setPrice(new BigDecimal(data.getPrice()));
            output.setCurrency(data.getCurrency());
            output.setNotes(data.getNotes());
            if (data.getStartDate() != null) {
                output.setStartDate(data.getStartDate().format(DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)));
            }
            if (data.getEndDate() != null) {
                output.setEndDate(data.getEndDate().format(DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return output;
  }
}
