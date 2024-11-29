package vn.com.atomi.loyalty.core.service.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.base.utils.RequestUtils;
import vn.com.atomi.loyalty.core.dto.input.NotificationInput;
import vn.com.atomi.loyalty.core.dto.input.TransactionInput;
import vn.com.atomi.loyalty.core.dto.input.UsePointInput;
import vn.com.atomi.loyalty.core.dto.message.AllocationPointTransactionInput;
import vn.com.atomi.loyalty.core.dto.output.*;
import vn.com.atomi.loyalty.core.dto.projection.CustomerBalanceProjection;
import vn.com.atomi.loyalty.core.entity.CustomerBalance;
import vn.com.atomi.loyalty.core.entity.CustomerBalanceHistory;
import vn.com.atomi.loyalty.core.enums.*;
import vn.com.atomi.loyalty.core.feign.LoyaltyCollectDataClient;
import vn.com.atomi.loyalty.core.feign.LoyaltyConfigClient;
import vn.com.atomi.loyalty.core.feign.LoyaltyEventGetwayClient;
import vn.com.atomi.loyalty.core.repository.CustomRepository;
import vn.com.atomi.loyalty.core.repository.CustomerBalanceHistoryRepository;
import vn.com.atomi.loyalty.core.repository.CustomerBalanceRepository;
import vn.com.atomi.loyalty.core.repository.PointExpiredHistoryRepository;
import vn.com.atomi.loyalty.core.service.CustomerBalanceService;
import vn.com.atomi.loyalty.core.service.CustomerService;
import vn.com.atomi.loyalty.core.utils.Constants;
import vn.com.atomi.loyalty.core.utils.Utils;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author haidv
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class CustomerBalanceServiceImpl extends BaseService implements CustomerBalanceService {

    private final CustomerBalanceRepository customerBalanceRepository;

    private final CustomerBalanceHistoryRepository customerBalanceHistoryRepository;

    private final CustomRepository customRepository;

    private final LoyaltyConfigClient loyaltyConfigClient;

    private final LoyaltyCollectDataClient loyaltyCollectDataClient;

    private final PointExpiredHistoryRepository pointExpiredHistoryRepository;

    private final CustomerService customerService;

    private final LoyaltyEventGetwayClient loyaltyEventGetwayClient;

    private final EntityManager entityManager;

    @Override
    public CustomerBalanceOutput getCurrentBalance(String cifBank, String cifWallet) {
        // bắt buộc truyền 1 trong 2 param
        if (StringUtils.isBlank(cifBank) && StringUtils.isBlank(cifWallet)) {
            throw new BaseException(ErrorCode.INPUT_INVALID);
        }
        CustomerBalanceProjection balanceProjection =
                customerBalanceRepository.findCurrentBalance(cifBank, cifWallet);
        if (balanceProjection == null || balanceProjection.getCustomerId() == null) {
            throw new BaseException(ErrorCode.CUSTOMER_NOT_EXISTED);
        }
        // TODO
        return super.modelMapper.convertToCustomerBalanceOutput(balanceProjection);
    }

    @Override
    public ResponsePage<ExternalCustomerBalanceHistoryOutput> getBalanceHistories(
            Long customerId,
            ChangeType changeType,
            String startTransactionDate,
            String endTransactionDate,
            Pageable pageable) {
        var page =
                customerBalanceHistoryRepository.findHistory(
                        customerId,
                        changeType,
                        PointType.CONSUMPTION_POINT,
                        Utils.reformatStringDate(
                                startTransactionDate,
                                DateConstant.STR_PLAN_DD_MM_YYYY_STROKE,
                                DateConstant.ISO_8601_EXTENDED_DATE_FORMAT_STROKE),
                        Utils.reformatStringDate(
                                endTransactionDate,
                                DateConstant.STR_PLAN_DD_MM_YYYY_STROKE,
                                DateConstant.ISO_8601_EXTENDED_DATE_FORMAT_STROKE),
                        null,
                        null,
                        pageable);
        return new ResponsePage<>(
                page, super.modelMapper.convertToExternalCustomerBalanceHistoryOutputs(page.getContent()));
    }

    @Override
    public ResponsePage<CustomerBalanceHistoryOutput> getBalanceHistories(
            Long customerId,
            ChangeType changeType,
            PointType pointType,
            String startTransactionDate,
            String endTransactionDate,
            String startExpiredDate,
            String endExpiredDate,
            Pageable pageable) {
        var page =
                customerBalanceHistoryRepository.findHistory(
                        customerId,
                        changeType,
                        pointType,
                        Utils.reformatStringDate(
                                startTransactionDate,
                                DateConstant.STR_PLAN_DD_MM_YYYY_STROKE,
                                DateConstant.ISO_8601_EXTENDED_DATE_FORMAT_STROKE),
                        Utils.reformatStringDate(
                                endTransactionDate,
                                DateConstant.STR_PLAN_DD_MM_YYYY_STROKE,
                                DateConstant.ISO_8601_EXTENDED_DATE_FORMAT_STROKE),
                        Utils.convertToLocalDate(startExpiredDate),
                        Utils.convertToLocalDate(endExpiredDate),
                        pageable);
        return new ResponsePage<>(
                page, super.modelMapper.convertToCustomerBalanceHistoryOutputs(page.getContent()));
    }

    @Override
    public void executeTransactionMinus(UsePointInput usePointInput) {
    }

//  @Override
//  public void executePointExpiration() {
//    pointExpiredHistoryRepository
//        .findTop1ByDeletedFalseOrderByExpiredAtDesc()
//        .ifPresentOrElse(
//            pointExpiredHistory -> {
//              if (pointExpiredHistory.getEndAt() == null) {
//                LOGGER.info("Another process is running");
//              } else {
//                if (pointExpiredHistory.getExpiredAt().isBefore(LocalDate.now())) {
//                  LOGGER.info("Processed up to the latest date");
//                } else {
//                  customRepository.expiredAmount(
//                      UUID.randomUUID().toString(),
//                      pointExpiredHistory.getExpiredAt().plusDays(1),
//                      Constants.EXPIRED_POINT_CONTENT,
//                      PointType.CONSUMPTION_POINT);
//                }
//              }
//            },
//            () -> {
//              LOGGER.info("Perform processing for the first time");
//              customRepository.expiredAmount(
//                  UUID.randomUUID().toString(),
//                  LocalDate.now().minusDays(1),
//                  Constants.EXPIRED_POINT_CONTENT,
//                  PointType.CONSUMPTION_POINT);
//            });
//  }

    @Override
    public void executePointExpiration() {
        List<CustomerBalanceHistory> histories = customerBalanceHistoryRepository.findTranByPointType(55L, PointType.CONSUMPTION_POINT);
        if (histories.isEmpty()) {
            LOGGER.info("Perform processing for the first time");
            customRepository.expiredAmount(
                    UUID.randomUUID().toString(),
                    LocalDate.now().minusDays(1),
                    Constants.EXPIRED_POINT_CONTENT,
                    PointType.CONSUMPTION_POINT);
        } else {
            for (CustomerBalanceHistory history : histories) {
                if (history.getExpireAt() == null) {
                    LOGGER.info("Another process is running");
                } else if (history.getExpireAt().isBefore(LocalDate.now())) {
                    customRepository.expiredAmount(
                            UUID.randomUUID().toString(),
                            history.getExpireAt().plusDays(1),
                            Constants.EXPIRED_POINT_CONTENT,
                            PointType.CONSUMPTION_POINT);
                } else {
                    LOGGER.info("Processed up to the latest date");
                }
            }
        }
    }

    @Override
    public void calculatePointCasa() {
        RulePOCOutput rulePOC = loyaltyConfigClient.getRulePoc(RequestUtils.extractRequestId(), "CASA").getData();
        RuleOutput rule = this.convertToRuleOutput(rulePOC);
        List<CustomerCasa> customerCasas = loyaltyCollectDataClient.getLstCurrentCasa(RequestUtils.extractRequestId()).getData()
                .stream()
                .filter(customerCasa -> customerCasa.getCasaAmount() >= rulePOC.getMinTransaction())
                .collect(Collectors.toList());
        for (CustomerCasa customerCasa : customerCasas) {
            // lấy thông tin của KH
            CustomerOutput customerOutput = customerService.getCustomer(customerCasa.getCifBank(), null);
            // lấy thông tin tài khoản điểm của KH
            CustomerBalance customerBalance =
                    customerBalanceRepository
                            .findByDeletedFalseAndCustomerId(customerOutput.getId())
                            .orElse(null);
            if (customerBalance == null) {
                LOGGER.warn("Not found customer balance with customerId: {}", customerOutput.getId());
            }
            LocalDateTime transactionDate = LocalDateTime.now();
            var amount = BigInteger.valueOf(customerCasa.getCasaAmount());
            long limitPoint = rulePOC.getLimitPointPerUser();
            String refNo = UUID.randomUUID().toString();
            AllocationPointTransactionInput allocationTransaction = new AllocationPointTransactionInput();
            allocationTransaction.setAmount(Long.valueOf(String.valueOf(amount)));
            allocationTransaction.setRefNo(refNo);
            allocationTransaction.setTransactionAt(transactionDate);
            allocationTransaction.setCurrency("VND");
            allocationTransaction.setTransactionType("CASA");
            allocationTransaction.setTransactionGroup("FUNDTF");
            List<TransactionInput> results = new ArrayList<>();
            // tính điểm
            var totalPoint = BigInteger.ZERO;
            var basePoint = BigInteger.ZERO;
            long consumptionPoint = 0;
            basePoint =
                    amount
                            .divide(BigInteger.valueOf(rulePOC.getExchangeValue()))
                            .multiply(BigInteger.valueOf(rulePOC.getExchangePoint()));
            totalPoint = totalPoint.add(basePoint);
            consumptionPoint = totalPoint.longValue();
            results.add(
                    super.modelMapper.convertToTransactionInput(
                            allocationTransaction,
                            PointType.CONSUMPTION_POINT,
                            consumptionPoint,
                            customerOutput.getId(),
                            rule,
                            PointEventSource.LV24H,
                            this.getExpireDate(rule)));
            List<Long> transactionIds = customRepository.plusAmounts(results);
            if (transactionIds.size() > 0) {
                NotificationInput notificationInput = this.convertInput(consumptionPoint, customerOutput);
                loyaltyEventGetwayClient.sendNotification(RequestUtils.extractRequestId(), notificationInput);
            }

        }
    }

    @Override
    public void calculatePointCurrencyTransaction(String startDate, String endDate) {
        RulePOCOutput rulePOC = loyaltyConfigClient.getRulePoc(RequestUtils.extractRequestId(), "NGOAITE").getData();
        RuleOutput rule = this.convertToRuleOutput(rulePOC);
        List<CurrencyTransaction> currencyTransactions = loyaltyCollectDataClient.getCustomerCurrencyTransactions(RequestUtils.extractRequestId(), startDate, endDate).getData()
                .stream()
                .filter(currencyTransaction -> currencyTransaction.getCurrencyAmount() >= rulePOC.getMinTransaction())
                .collect(Collectors.toList());
        for (CurrencyTransaction currencyTransaction : currencyTransactions) {
            // lấy thông tin của KH
            CustomerOutput customerOutput = customerService.getCustomer(currencyTransaction.getCifBank(), null);
            // lấy thông tin tài khoản điểm của KH
            CustomerBalance customerBalance =
                    customerBalanceRepository
                            .findByDeletedFalseAndCustomerId(customerOutput.getId())
                            .orElse(null);
            if (customerBalance == null) {
                LOGGER.warn("Not found customer balance with customerId: {}", customerOutput.getId());
            }
            LocalDateTime transactionDate = LocalDateTime.now();
            var amount = BigInteger.valueOf(currencyTransaction.getCurrencyAmount());
            long limitPointTransaction = rulePOC.getLimitPointPerTransaction();
            String refNo = UUID.randomUUID().toString();
            AllocationPointTransactionInput allocationTransaction = new AllocationPointTransactionInput();
            allocationTransaction.setAmount(Long.valueOf(String.valueOf(amount)));
            allocationTransaction.setRefNo(refNo);
            allocationTransaction.setTransactionAt(transactionDate);
            allocationTransaction.setCurrency(currencyTransaction.getCurrencyUnit());
            allocationTransaction.setTransactionType("NGOAITE");
            allocationTransaction.setTransactionGroup("FUNDTF");
            List<TransactionInput> results = new ArrayList<>();
            // tính điểm
            var totalPoint = BigInteger.ZERO;
            var basePoint = BigInteger.ZERO;
            long consumptionPoint = 0;
            basePoint =
                    amount
                            .divide(BigInteger.valueOf(rulePOC.getExchangeValue()))
                            .multiply(BigInteger.valueOf(rulePOC.getExchangePoint()));
            totalPoint = totalPoint.add(basePoint);
            consumptionPoint = totalPoint.longValue();
            if (consumptionPoint > limitPointTransaction) {
                consumptionPoint = limitPointTransaction;
            }
            results.add(
                    super.modelMapper.convertToTransactionInput(
                            allocationTransaction,
                            PointType.CONSUMPTION_POINT,
                            consumptionPoint,
                            customerOutput.getId(),
                            rule,
                            PointEventSource.LV24H,
                            this.getExpireDate(rule)));
            List<Long> transactionIds = customRepository.plusAmounts(results);
            if (transactionIds.size() > 0) {
                NotificationInput notificationInput = this.convertInput(consumptionPoint, customerOutput);
                loyaltyEventGetwayClient.sendNotification(RequestUtils.extractRequestId(), notificationInput);
            }
        }
    }

    @Override
    public void calculatePointCard(String startDate, String endDate) {
        RulePOCOutput rulePOC = loyaltyConfigClient.getRulePoc(RequestUtils.extractRequestId(), "CARD").getData();
        RuleOutput rule = this.convertToRuleOutput(rulePOC);
        List<CardTransactionInfo> cardTransactionInfos = loyaltyCollectDataClient.getLstCardTransaction(RequestUtils.extractRequestId(), startDate, endDate).getData()
                .stream()
                .filter(cardTransactionInfo -> Long.parseLong(cardTransactionInfo.getCif()) >= rulePOC.getMinTransaction())
                .collect(Collectors.toList());
        for (CardTransactionInfo cardTransactionInfo : cardTransactionInfos) {
            // lấy thông tin của KH
            CustomerOutput customerOutput = customerService.getCustomer(cardTransactionInfo.getCustomerName(), null);
            // lấy thông tin tài khoản điểm của KH
            CustomerBalance customerBalance =
                    customerBalanceRepository
                            .findByDeletedFalseAndCustomerId(customerOutput.getId())
                            .orElse(null);
            if (customerBalance == null) {
                LOGGER.warn("Not found customer balance with customerId: {}", customerOutput.getId());
            }
            LocalDateTime transactionDate = LocalDateTime.now();
            var amount = BigInteger.valueOf(Long.parseLong(cardTransactionInfo.getTotalAmount()));
            long limitPoint = rulePOC.getLimitPointPerUser();
            String refNo = UUID.randomUUID().toString();
            AllocationPointTransactionInput allocationTransaction = new AllocationPointTransactionInput();
            allocationTransaction.setAmount(Long.valueOf(String.valueOf(amount)));
            allocationTransaction.setRefNo(refNo);
            allocationTransaction.setTransactionAt(transactionDate);
            allocationTransaction.setCurrency("VND");
            allocationTransaction.setTransactionType("CARD");
            allocationTransaction.setTransactionGroup("FUNDTF");
            List<TransactionInput> results = new ArrayList<>();
            // tính điểm
            var totalPoint = BigInteger.ZERO;
            var basePoint = BigInteger.ZERO;
            long consumptionPoint = 0;
            basePoint =
                    amount
                            .divide(BigInteger.valueOf(rulePOC.getExchangeValue()))
                            .multiply(BigInteger.valueOf(rulePOC.getExchangePoint()));
            totalPoint = totalPoint.add(basePoint);
            consumptionPoint = totalPoint.longValue();
            results.add(
                    super.modelMapper.convertToTransactionInput(
                            allocationTransaction,
                            PointType.CONSUMPTION_POINT,
                            consumptionPoint,
                            customerOutput.getId(),
                            rule,
                            PointEventSource.LV24H,
                            this.getExpireDate(rule)));
            List<Long> transactionIds = customRepository.plusAmounts(results);
            if (transactionIds.size() > 0) {
                NotificationInput notificationInput = this.convertInput(consumptionPoint, customerOutput);
                loyaltyEventGetwayClient.sendNotification(RequestUtils.extractRequestId(), notificationInput);
            }

        }
    }

    @Override
    public void calculateCompleteBiometric() {

        RulePOCOutput rulePOC = loyaltyConfigClient.getRulePoc(RequestUtils.extractRequestId(), "BIOMETRIC").getData();
        RuleOutput rule = this.convertToRuleOutput(rulePOC);

        List<EGCBiometricOutput> egcBiometricOutputs = loyaltyEventGetwayClient.getLstCompleteBiometric(RequestUtils.extractRequestId()).getData()
                .stream()
                .filter(egcBiometricOutput -> egcBiometricOutput.getIsPlusPoint() == false)
                .collect(Collectors.toList());

        for (EGCBiometricOutput egcBiometricOutput : egcBiometricOutputs) {
            // lấy thông tin của KH
            CustomerOutput customerOutput = customerService.getCustomer(egcBiometricOutput.getCifBank(), null);
            // lấy thông tin tài khoản điểm của KH
            CustomerBalance customerBalance =
                    customerBalanceRepository
                            .findByDeletedFalseAndCustomerId(customerOutput.getId())
                            .orElse(null);
            if (customerBalance == null) {
                LOGGER.warn("Not found customer balance with customerId: {}", customerOutput.getId());
                return;
            }

            LocalDateTime transactionDate = LocalDateTime.now();
            long limitPoint = rulePOC.getLimitPointPerUser();
            String refNo = UUID.randomUUID().toString();
            AllocationPointTransactionInput allocationTransaction = new AllocationPointTransactionInput();
            allocationTransaction.setAmount(Long.valueOf(String.valueOf(limitPoint)));
            allocationTransaction.setRefNo(refNo);
            allocationTransaction.setTransactionAt(transactionDate);
            allocationTransaction.setCurrency("VND");
            allocationTransaction.setTransactionType("BIOMETRIC");
            allocationTransaction.setTransactionGroup("FUNDTF");
            List<TransactionInput> results = new ArrayList<>();
            // tính điểm
            var totalPoint = BigInteger.ZERO;
            var basePoint = BigInteger.ZERO;
            long consumptionPoint = 0;
            basePoint = BigInteger.valueOf(limitPoint);
            totalPoint = totalPoint.add(basePoint);
            consumptionPoint = totalPoint.longValue();
            results.add(
                    super.modelMapper.convertToTransactionInput(
                            allocationTransaction,
                            PointType.CONSUMPTION_POINT,
                            consumptionPoint,
                            customerOutput.getId(),
                            rule,
                            PointEventSource.LV24H,
                            this.getExpireDate(rule)));
            List<Long> transactionIds = customRepository.plusAmounts(results);
            if (transactionIds.size() > 0) {
                NotificationInput notificationInput = this.convertInput(consumptionPoint, customerOutput);
                loyaltyEventGetwayClient.sendNotification(RequestUtils.extractRequestId(), notificationInput);

                loyaltyEventGetwayClient.automaticupdate(RequestUtils.extractRequestId(), customerOutput.getCifBank());
            }
        }
    }


    public static NotificationInput convertInput(long consumptionPoint, CustomerOutput customerOutput) {
        NotificationInput notificationInput = new NotificationInput();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss.SSS");
        String startTime = LocalDateTime.now().format(formatter);
        long date = new Date().getTime();
        notificationInput.setLanguage("VN");
        notificationInput.setRequestId(String.valueOf(date));
        notificationInput.setClientTime(startTime);
        notificationInput.setTransTime(String.valueOf(date));
        notificationInput.setTitle("Cộng điểm Loyalty");
        notificationInput.setContent("Quý khách được cộng " + consumptionPoint + " điểm Loyalty");
        notificationInput.setUserName(customerOutput.getPhone());
        return notificationInput;
    }

    public static RuleOutput convertToRuleOutput(RulePOCOutput rulePOC) {
        return RuleOutput.builder()
                .id(rulePOC.getId())
                .type(rulePOC.getType())
                .code(rulePOC.getCode())
                .name(rulePOC.getName())
                .pointType(rulePOC.getPointType())
                .campaignId(rulePOC.getCampaignId())
                .campaignCode(rulePOC.getCampaignCode())
                .budgetId(rulePOC.getBudgetId())
                .budgetCode(rulePOC.getCode())
                .startDate(rulePOC.getStartDate())
                .endDate(rulePOC.getEndDate())
                .status(rulePOC.getStatus())
                .expirePolicyType(ExpirePolicyType.NEVER)
                .expirePolicyValue(null)
                .ruleAllocationOutputs(List.of())
                .ruleBonusOutputs(List.of())
                .ruleConditionOutputs(List.of())
                .build();
    }

    private LocalDate getExpireDate(RuleOutput ruleOutput) {
        return switch (ruleOutput.getExpirePolicyType()) {
            case AFTER_DATE -> Utils.convertToLocalDate(ruleOutput.getExpirePolicyValue());
            case AFTER_DAY -> LocalDate.now().plusDays(Long.parseLong(ruleOutput.getExpirePolicyValue()));
            case FIRST_DATE_OF_MONTH -> LocalDate.now()
                    .plusMonths(Long.parseLong(ruleOutput.getExpirePolicyValue()))
                    .with(TemporalAdjusters.firstDayOfMonth());
            case NEVER -> null;
        };
    }
}
