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
import vn.com.atomi.loyalty.core.dto.input.PurchasePackageInput;
import vn.com.atomi.loyalty.core.dto.output.CChainMissionOuput;
import vn.com.atomi.loyalty.core.dto.output.CMissionOuput;
import vn.com.atomi.loyalty.core.dto.output.CustomerOutput;
import vn.com.atomi.loyalty.core.dto.projection.CustomerBalanceProjection;
import vn.com.atomi.loyalty.core.entity.*;
import vn.com.atomi.loyalty.core.enums.*;
import vn.com.atomi.loyalty.core.enums.Currency;
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
import java.util.*;
import java.util.concurrent.CompletableFuture;
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

    private final CustomerBalanceRepository customerBalanceRepository;

    private final NotificationService notificationService;

    private final TransExternalRepository transExternalRepository;

    @Override
    public List<CChainMissionOuput> getNewChainMission() {
    var chainMission = chainMissionRepository.getNewChainMission(Chain.Y, Status.ACTIVE);
    return super.modelMapper.convertChainMissionOutput(chainMission);
    }
    @Override
    public List<CChainMissionOuput> getRegistedChainMission(String cifNo, String status) {
        Optional<Customer> customer = customerRepository.findByCifBank(cifNo);
        if(!customer.isPresent()) {
            throw new BaseException(ErrorCode.CUSTOMER_NOT_EXISTED);
        }
        var chainMission = chainMissionRepository.getRegistedChainMission(customer.get().getId(), status);
        return this.mappingMissionProgress(chainMission);

    }
    @Override
    public List<CMissionOuput> getListMission(Long chainId) {
        var lstMission = missionRepository.getListMission(chainId);
        if (!lstMission.isEmpty()) {
            return this.mappingListMission(lstMission);
        }
        return new ArrayList<>();
    }
    @Override
    public List<CMissionOuput> getListMissionInProgress(String cifNo, Long chainId) {
        List<CMissionOuput> response = new ArrayList<>();
        var lstMission = missionRepository.getListMissionInProgress(cifNo, chainId);
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
    public String purchaseChainMission(PurchaseChainMissionInput purchaseChainMission) {
        // kiem tra khach hang ton tai
        String responseId = "";
        Optional<Customer> customer = customerRepository.findByCifBank(purchaseChainMission.getCifNo());
        if(!customer.isPresent()) {
            throw new BaseException(ErrorCode.CUSTOMER_NOT_EXISTED);
        }
        purchaseChainMission.setCustomerId(customer.get().getId());
        // kiem tra diem kha dung neu dang ky goi bang diem loyalty
        if(Currency.PNT.name().equalsIgnoreCase(purchaseChainMission.getTxnCurrency())) {
            CustomerBalanceProjection customerBalance = customerBalanceRepository.findCurrentBalance(purchaseChainMission.getCifNo(), null);
            if (customerBalance == null) {
                throw new BaseException(ErrorCode.CUSTOMER_NOT_EXISTED);
            } else if (customerBalance.getAvailableAmount() < purchaseChainMission.getTxnAmount()) {
                throw new BaseException(ErrorCode.NOT_ENOUGH_BALANCE);
            }
        }

        var chainMission = chainMissionRepository.findById(purchaseChainMission.getChainId());
        if(!chainMission.isPresent()) {
            throw new BaseException(ErrorCode.CHAIN_MISSION_NOT_FOUND);
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
        List<CCustMissionProgress> lstMissionProgress = this.saveMissonProgress(purchaseChainMission, customer.get());
        if(Currency.PNT.name().equalsIgnoreCase(purchaseChainMission.getTxnCurrency())) {
            // truong hop thanh toan bang point call minus point
            Long transactionId = customRepository.minusAmount(
                    purchaseChainMission.getCustomerId(),
                    purchaseChainMission.getTxnAmount(),
                    purchaseChainMission.getRefNo(),
                    LocalDateTime.parse(purchaseChainMission.getTransactionAt(),
                            DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS_STROKE)),
                    purchaseChainMission.getNotes(),
                    PointType.CONSUMPTION_POINT);

            if (transactionId > 0) {
                CompletableFuture.runAsync(() -> {
                    try {
                        this.handleNotification(customer.get(), purchaseChainMission, chainMission.get());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("Push notification app LPB - Transaction Id: " + transactionId + " already done!");
                });
            }
        } else {
            // truong hop thanh toan bang tien => luu thong tin xuong bang history
            TransExternal history = transExternalRepository.save(mappingTransactionExternal(purchaseChainMission, customer.get()));
            if(history != null) {
                responseId = history.getId();
            } else {
                throw new BaseException(ErrorCode.CHAIN_MISSION_ERROR);
            }
        }
        return responseId;
    }
    public void finishMission(Long missionId, Long chainId, String  cifNo) {
        cCustMissionProgressRepository.finishMission(missionId, chainId, cifNo, Constants.Mission.STATUS_COMPLETED);
    }
    private List<CCustMissionProgress> saveMissonProgress (PurchaseChainMissionInput purchaseChainMission, Customer customer){
      // tao chuoi nhiem vu gan theo khach hang
      var chainMission = cCustMissionProgressRepository.
              getDataChainMission(
                      purchaseChainMission.getRefNo(),
                      purchaseChainMission.getCifNo(),
                      purchaseChainMission.getChainId());
      if(chainMission == null) {
          throw new BaseException(ErrorCode.CHAIN_MISSION_NOT_FOUND);
      }
      // save data
      return cCustMissionProgressRepository.saveAll(this.mappingChainMissionToProgress(chainMission, customer));

    }

    private List<CChainMissionOuput> mappingMissionProgress (List<CChainMission> rawData){
        return rawData.stream()
                .map(data -> {
                    CChainMissionOuput output = new CChainMissionOuput();

                    output.setId(data.getId());
                    output.setCode(data.getCode());
                    output.setName(data.getName());
                    output.setGroupType(data.getGroupType());
                    output.setBenefitType(data.getBenefitType());
                    output.setImage(data.getImage());
                    output.setIsOrdered(data.getIsOrdered());
                    output.setPrice(data.getPrice());
                    output.setCurrency(data.getCurrency());
                    output.setNotes(data.getNotes());
                    if(data.getStartDate() != null) {
                        output.setStartDate(data.getStartDate().format(DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)));
                    }
                    if (data.getEndDate() != null) {
                        output.setEndDate(data.getEndDate().format(DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)));
                    }
                    return output;

                }).collect(Collectors.toList());
    }

    private List<CMissionOuput> mappingListMission (List<Object[]> rawData){
      return rawData.stream()
          .map(data -> {
                CMissionOuput output = new CMissionOuput();

                output.setChainId(data.length > 0 ? Long.valueOf((String) data[0]) : null);  // Assuming column 0 is chainId
                output.setId(data.length > 1 ? Long.valueOf((String) data[1]) : null);  // Assuming column 1 is missionId
                output.setOrderNo(data.length > 2 ? Integer.parseInt((String) data[2]) : null);  // Assuming column 2 is orderNo
                output.setGroupType(data.length > 3 ? (String) data[3] : null);  // Assuming column 3 is groupType
                output.setCode(data.length > 4 ? (String) data[4] : null);  // Assuming column 4 is code
                output.setName(data.length > 5 ? (String) data[5] : null);  // Assuming column 5 is name
                output.setBenefitType(data.length > 6 ? (String) data[6] : null);  // Assuming column 6 is benefitType
                if (data.length > 7 && data[7] != null) {
                    Timestamp startDate = (Timestamp) data[7];
                    output.setStartDate(new SimpleDateFormat(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE).format(startDate.getTime()));
                }
                if (data.length > 8 && data[8] != null) {
                    Timestamp endDate = (Timestamp) data[8];
                    output.setEndDate(new SimpleDateFormat(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE).format(endDate.getTime()));
                }
                output.setPrice(data.length == 9? new BigDecimal((String) data[9]) : null);  // Assuming column 9 is price
                output.setCurrency(data.length > 10 ? (String) data[10] : null);  // Assuming column 10 is currency
                output.setImage(data.length > 11 ? (String) data[11] : null);  // Assuming column 11 is image
                output.setNotes(data.length > 12 ? (String) data[12] : null);  // Assuming column 12 is notes
                output.setStatus(data.length > 13 ? (String) data[13] : null);  // Assuming column 13 is notes
                return output;

          }).collect(Collectors.toList());
    }

    private List<CCustMissionProgress> mappingChainMissionToProgress (List<CCustMissionProgress> rawData, Customer customer){
        return rawData.stream()
                .map(data -> {
                    CCustMissionProgress output = new CCustMissionProgress();

                    output.setCustomerId(customer.getId());
                    output.setCifNo(customer.getCifBank());
                    output.setParentId(data.getParentId());
                    output.setMissionId(data.getMissionId());
                    output.setMissionType(data.getMissionType());
                    output.setOrderNo(data.getOrderNo());
                    output.setGroupType(data.getGroupType());
                    output.setStartDate(data.getStartDate());
                    output.setEndDate(data.getEndDate());
                    output.setStatus(Constants.Mission.STATUS_INPROGRESS);
                    output.setTxnRefNo(data.getTxnRefNo());
                    return output;

                }).collect(Collectors.toList());
    }

    private TransExternal mappingTransactionExternal(PurchaseChainMissionInput purchaseChainMission, Customer customer){
        TransExternal response = new TransExternal();
        try {
            response.setId(UUID.randomUUID().toString());
            response.setCustomerId( customer.getId() );
            response.setPhoneNo( customer.getPhone() );
            response.setCifNo( purchaseChainMission.getCifNo() );
            response.setRefId( purchaseChainMission.getChainId() );
            response.setRefType( RefType.MISSION );
            response.setTxnRefNo( purchaseChainMission.getRefNo() );
            response.setTxnId( purchaseChainMission.getTransId() );
            if (StringUtils.isNotBlank(purchaseChainMission.getTransactionAt())) {
                response.setTxnDate(
                        LocalDate.parse(purchaseChainMission.getTransactionAt()
                        , DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS_STROKE)));
            }
            response.setTxnAmount( purchaseChainMission.getTxnAmount() );
            response.setTxnStatus(Constants.Status.SUCCESS);
            response.setTxnCurrency( purchaseChainMission.getTxnCurrency() );
            response.setTxnMethod( purchaseChainMission.getPaymentMethod() );
            response.setTxnChannel( purchaseChainMission.getPaymentChannel() );
            response.setTxnNote( purchaseChainMission.getNotes() );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response;
    }

    public void handleNotification(Customer customer,
                                   PurchaseChainMissionInput purchaseChainMission,
                                   CChainMission chainMission) {
        notificationService.sendNotification(
                Constants.Notification.MISSION_TITLE,
                Constants.Notification.MISSION_CONTENT + " " + chainMission.getName(),
                customer.getPhone());
    }

}
