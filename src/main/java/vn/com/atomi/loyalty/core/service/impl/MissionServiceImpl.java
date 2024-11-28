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
import vn.com.atomi.loyalty.base.utils.RequestUtils;
import vn.com.atomi.loyalty.core.dto.input.NotificationInput;
import vn.com.atomi.loyalty.core.dto.input.PurchaseChainMissionInput;
import vn.com.atomi.loyalty.core.dto.output.CChainMissionOuput;
import vn.com.atomi.loyalty.core.dto.output.CMissionOuput;
import vn.com.atomi.loyalty.core.dto.output.CustomerOutput;
import vn.com.atomi.loyalty.core.dto.projection.CustomerBalanceProjection;
import vn.com.atomi.loyalty.core.entity.CCustMissionProgress;
import vn.com.atomi.loyalty.core.entity.CMission;
import vn.com.atomi.loyalty.core.entity.CMissionSequential;
import vn.com.atomi.loyalty.core.entity.Customer;
import vn.com.atomi.loyalty.core.enums.*;
import vn.com.atomi.loyalty.core.feign.LoyaltyEventGetwayClient;
import vn.com.atomi.loyalty.core.repository.*;
import vn.com.atomi.loyalty.core.service.MissionService;
import vn.com.atomi.loyalty.core.service.NotificationService;
import vn.com.atomi.loyalty.core.utils.Constants;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

  private final CustomRepository customRepository;

  private final CCustMissionProgressRepository cCustMissionProgressRepository;

  private final CMissionSequentialRepository missionSequentialRepository;

  private final CustomerBalanceRepository customerBalanceRepository;

  private final NotificationService notificationService;

  @Override
  public List<CChainMissionOuput> getNewChainMission() {
    var chainMission = chainMissionRepository.getNewChainMission(Chain.Y, Status.ACTIVE);
    return super.modelMapper.convertChainMissionOutput(chainMission);
  }
  @Override
  public List<CChainMissionOuput> getRegistedChainMission(String cifNo) {
        Optional<Customer> customer = customerRepository.findByCifBank(cifNo);
        if(customer.isPresent()) {
        var chainMission = chainMissionRepository.getRegistedChainMission(customer.get().getId());
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
  public CChainMissionOuput getChainMissionDetail(Long id) {
    var mission = chainMissionRepository.getChainMissionDetail(id, Status.ACTIVE);
    return modelMapper.convertChainMissionDetailOutput(mission);
  }
  @Override
  public CMissionOuput getMissionDetail(Long id) {
    var mission = missionRepository.getMissionDetail(id, Status.ACTIVE);
    return modelMapper.convertMissionDetailOutput(mission);
  }
  @Override
  public Long purchaseChainMission(PurchaseChainMissionInput purchaseChainMission) {
        // kiem tra khach hang ton tai
        Long transId = null;
        Optional<Customer> customer = customerRepository.findByCifBank(purchaseChainMission.getCifNo());
        if(!customer.isPresent()) {
        throw new BaseException(ErrorCode.CUSTOMER_NOT_EXISTED);
        }
        purchaseChainMission.setCustomerId(customer.get().getId());
        // kiem tra diem kha dung
        CustomerBalanceProjection customerBalance = customerBalanceRepository.findCurrentBalance(purchaseChainMission.getCifNo(), null);
        if(customerBalance == null) {
        throw new BaseException(ErrorCode.CUSTOMER_NOT_EXISTED);
        } else if(customerBalance.getAvailableAmount() < purchaseChainMission.getTxnAmount()) {
          throw new BaseException(ErrorCode.CUSTOMER_BALANCE_NOT_ENOUGH);
        }

        // kiem tra khach hang da dang ky goi nhiem vu truoc do
        CCustMissionProgress missionProgress = cCustMissionProgressRepository.
            findByCustomerAndChainId(
                    customer.get().getId(),
                    purchaseChainMission.getChainId(),
                    Constants.Mission.TYPE_CHAIN,
                    Constants.Mission.STATUS_PENDING);
        if(missionProgress != null) {
        throw new BaseException(ErrorCode.CUSTOMER_REGISTED_CHAIN_MISSION);
        }
        // tao chuoi nhiem vu gan theo khach hang
        List<CCustMissionProgress> lstMissionProgress = this.saveMissonProgress(purchaseChainMission);
        if(Currency.PNT.name().equalsIgnoreCase(purchaseChainMission.getTxnCurrency())) {
            // truong hop thanh toan bang point call minus point
            transId = customRepository.minusAmount(
                    purchaseChainMission.getCustomerId(),
                    purchaseChainMission.getTxnAmount(),
                    purchaseChainMission.getRefNo(),
                    LocalDateTime.parse(purchaseChainMission.getTransactionAt(),
                            DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS_STROKE)),
                    purchaseChainMission.getContent(),
                    PointType.CONSUMPTION_POINT);

            if (transId > 0) {
                notificationService.sendNotification(
                        Constants.Notification.PLUS,
                        purchaseChainMission.getTxnAmount(),
                        customer.get().getPhone());
            }
        } else {
            // truong hop thanh toan bang tien => luu thong tin xuong bang history
        }
        return transId;
  }

  private List<CCustMissionProgress> saveMissonProgress (PurchaseChainMissionInput purchaseChainMission){
      // tao chuoi nhiem vu gan theo khach hang
      var chainMission = cCustMissionProgressRepository.
              getDataChainMission(
                      purchaseChainMission.getRefNo(),
                      purchaseChainMission.getCifNo(),
                      purchaseChainMission.getChainId());

      // save data
      return cCustMissionProgressRepository.saveAll(this.mappingChainMissionToProgress(chainMission));

  }
  private List<CChainMissionOuput> mappingMissionProgress (List<Object[]> rawData){
      return rawData.stream()
          .map(data -> {
              CChainMissionOuput output = new CChainMissionOuput();

                output.setId((Long) data[0]);  // Assuming column 0 is chainId
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

                output.setChainId((Long) data[0]);  // Assuming column 0 is chainId
                output.setId((Long) data[1]);  // Assuming column 1 is missionId
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

    private List<CCustMissionProgress> mappingChainMissionToProgress (List<CCustMissionProgress> rawData){
        return rawData.stream()
                .map(data -> {
                    CCustMissionProgress output = new CCustMissionProgress();

                    output.setCustomer(data.getCustomer());
                    output.setParentId(data.getParentId());
                    output.setMissionId(data.getMissionId());
                    output.setMissionType(data.getMissionType());
                    output.setOrderNo(data.getOrderNo());
                    output.setGroupType(data.getGroupType());
                    output.setStartDate(data.getStartDate());
                    output.setEndDate(data.getEndDate());
                    output.setStatus(Constants.Mission.STATUS_PENDING);
                    output.setTxnRefNo(data.getTxnRefNo());
                    return output;

                }).collect(Collectors.toList());
    }

}
