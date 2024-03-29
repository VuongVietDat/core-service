package vn.com.atomi.loyalty.core.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.utils.JsonUtils;
import vn.com.atomi.loyalty.core.dto.input.AllocationPointTransactionInput;
import vn.com.atomi.loyalty.core.dto.message.AllocationPointMessage;
import vn.com.atomi.loyalty.core.dto.output.*;
import vn.com.atomi.loyalty.core.entity.CustomerBalance;
import vn.com.atomi.loyalty.core.enums.*;
import vn.com.atomi.loyalty.core.repository.CustomerBalanceHistoryRepository;
import vn.com.atomi.loyalty.core.repository.CustomerBalanceRepository;
import vn.com.atomi.loyalty.core.service.AllocationPointService;
import vn.com.atomi.loyalty.core.service.MasterDataService;
import vn.com.atomi.loyalty.core.service.RuleService;
import vn.com.atomi.loyalty.core.utils.Constants;
import vn.com.atomi.loyalty.core.utils.Utils;

/**
 * @author haidv
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AllocationPointServiceImpl extends BaseService implements AllocationPointService {

  private final RuleService ruleService;

  private final MasterDataService masterDataService;

  private final CustomerBalanceRepository customerBalanceRepository;

  private final CustomerBalanceHistoryRepository customerBalanceHistoryRepository;

  @Override
  public void handlerAllocationPointEvent(AllocationPointMessage allocationPointMessage) {
    var transactionInput = allocationPointMessage.getTransaction();
    var customerInput = allocationPointMessage.getCustomer();
    var transactionDate = transactionInput.getTransactionAt().toLocalDate();
    // lấy tất cả danh sách quy tắc hiệu lực
    var rules =
        ruleService.getAllActiveRule(
            allocationPointMessage.getType(), Utils.formatLocalDateToString(transactionDate));
    if (CollectionUtils.isEmpty(rules)) {
      LOGGER.warn(
          "Not found rule with type: {} currently active", allocationPointMessage.getType());
      return;
    }
    // lấy thông tin tài khoản điểm của KH
    CustomerBalance customerBalance =
        customerBalanceRepository
            .findByDeletedFalseAndId(allocationPointMessage.getCustomer().getId())
            .orElse(null);
    if (customerBalance == null) {
      LOGGER.warn(
          "Not found customer balance with customerId: {}",
          allocationPointMessage.getCustomer().getId());
      return;
    }
    // lấy giới hạn điểm
    var dictionaries =
        masterDataService.getDictionary(
            Constants.DICTIONARY_LIMIT_POINT_PER_USER, Status.ACTIVE, false);
    if (transactionInput.getAmount() == null) {
      LOGGER.warn("Transaction amount must not be null");
      return;
    }
    var amount = BigInteger.valueOf(transactionInput.getAmount());
    long limitPoint = -1;
    if (!dictionaries.isEmpty()) {
      limitPoint = Long.parseLong(dictionaries.get(0).getValue());
    }
    List<AllocationPointTransactionInput> allocationPointTransactionInputs = new ArrayList<>();
    for (RuleOutput rule : rules) {
      // kiểm tra điều kiện của quy tắc
      if (!CollectionUtils.isEmpty(rule.getRuleConditionOutputs())
          && !this.checkCondition(transactionInput, customerInput, rule)) {
        LOGGER.warn("The conditions for applying the rule are not met");
        return;
      }
      // tính điểm
      var totalPoint = BigInteger.ZERO;
      var basePoint = BigInteger.ZERO;
      long rankPoint = 0;
      long consumptionPoint = 0;
      // với loại giao dịch thì chỉ có 1 loại phân bổ điểm
      RuleAllocationOutput ruleAllocationOutput = rule.getRuleAllocationOutputs().get(0);
      if (ruleAllocationOutput.getMinTransaction() != null
          && ruleAllocationOutput.getMinTransaction().compareTo(transactionInput.getAmount()) > 0) {
        LOGGER.info(
            "The minimum balance condition of the point allocation rule with action: {} is not met",
            ruleAllocationOutput.getAction());
        continue;
      }
      basePoint =
          amount
              .divide(BigInteger.valueOf(ruleAllocationOutput.getExchangePoint()))
              .multiply(BigInteger.valueOf(ruleAllocationOutput.getExchangeValue()));
      totalPoint = totalPoint.add(basePoint);
      // thưởng thêm
      if (!CollectionUtils.isEmpty(rule.getRuleBonusOutputs())) {
        totalPoint =
            totalPoint.add(
                this.calculatorBonus(
                    rule.getRuleBonusOutputs(),
                    basePoint,
                    customerInput,
                    transactionInput,
                    transactionDate));
      }
      rankPoint = totalPoint.longValue();
      consumptionPoint = totalPoint.longValue();
      // kiểm tra giới hạn điểm chỉ với loại điểm dùng để đổi quà
      if (rule.getPointType().equals(PointType.CONSUMPTION_POINT)
          || rule.getPointType().equals(PointType.ALL)) {
        // giới hạn tổng điểm theo user theo năm
        if (limitPoint == 0) {
          consumptionPoint = 0;
        }
        if (limitPoint > 0) {
          var remain = limitPoint - customerBalance.getYearAccumulated();
          if (remain <= 0) {
            consumptionPoint = 0;
          } else {
            consumptionPoint = Math.min(consumptionPoint, remain);
          }
        }
        // giới hạn theo quy tắc
        // giới hạn số điểm tối đa phân bổ trên một giao dịch
        if (ruleAllocationOutput.getLimitPointPerTransaction() != null) {
          consumptionPoint =
              Math.min(ruleAllocationOutput.getLimitPointPerTransaction(), consumptionPoint);
        }
        // giới hạn số điểm tối đa phân bổ trên một khách hàng
        if (ruleAllocationOutput.getLimitPointPerUser() != null) {
          var totalAccumulated =
              customerBalanceHistoryRepository.sumByCustomerIdAndRuleId(
                  customerInput.getId(), rule.getId(), PointType.CONSUMPTION_POINT);
          var remain = ruleAllocationOutput.getLimitEventPerUser() - totalAccumulated;
          if (remain <= 0) {
            consumptionPoint = 0;
          } else {
            consumptionPoint = Math.min(consumptionPoint, remain);
          }
        }
        // giới hạn số lần tối đa phân bổ trên một khách hàng
        if (ruleAllocationOutput.getLimitEventPerUser() != null
            && ruleAllocationOutput.getFrequencyLimitEventPerUser() != null) {
          LocalDateTime startDate =
              this.getStartDateCountTransaction(
                  transactionInput.getTransactionAt(),
                  ruleAllocationOutput.getFrequencyLimitEventPerUser());
          var countResult =
              customerBalanceHistoryRepository.countByCustomerIdAndRuleId(
                  customerInput.getId(),
                  rule.getId(),
                  PointType.CONSUMPTION_POINT,
                  startDate,
                  transactionInput.getTransactionAt());
          if (countResult >= ruleAllocationOutput.getLimitEventPerUser()) {
            consumptionPoint = 0;
          }
        }
      }
      if (rule.getPointType().equals(PointType.RANK_POINT)) {
        allocationPointTransactionInputs.add(
            AllocationPointTransactionInput.builder()
                .pointType(rule.getPointType())
                .amount(rankPoint)
                .build());
      }
      if (rule.getPointType().equals(PointType.CONSUMPTION_POINT)) {
        allocationPointTransactionInputs.add(
            AllocationPointTransactionInput.builder()
                .pointType(rule.getPointType())
                .amount(consumptionPoint)
                .build());
      }
      if (rule.getPointType().equals(PointType.ALL)) {
        allocationPointTransactionInputs.add(
            AllocationPointTransactionInput.builder()
                .pointType(rule.getPointType())
                .amount(rankPoint)
                .build());
        allocationPointTransactionInputs.add(
            AllocationPointTransactionInput.builder()
                .pointType(rule.getPointType())
                .amount(consumptionPoint)
                .build());
      }
    }
  }

  private boolean checkCondition(
      AllocationPointTransactionInput transactionInput,
      CustomerOutput customerInput,
      RuleOutput rule) {
    boolean result = true;
    for (RuleConditionOutput ruleConditionOutput : rule.getRuleConditionOutputs()) {
      if (ConditionProperties.CHANEL.equals(ruleConditionOutput.getProperties())) {
        if (!Objects.equals(transactionInput.getChanel(), ruleConditionOutput.getValue())) {
          result = false;
          LOGGER.warn(
              "Rule: {} condition properties: {} operator: {} value: {} not match input: {}",
              rule.getCode(),
              ruleConditionOutput.getProperties(),
              ruleConditionOutput.getOperators(),
              ruleConditionOutput.getValue(),
              transactionInput.getChanel());
        }
      } else if (ConditionProperties.GENDER.equals(ruleConditionOutput.getProperties())) {
        if (!Objects.equals(customerInput.getGender(), ruleConditionOutput.getValue())) {
          result = false;
          LOGGER.warn(
              "Rule: {} condition properties: {} operator: {} value: {} not match input: {}",
              rule.getCode(),
              ruleConditionOutput.getProperties(),
              ruleConditionOutput.getOperators(),
              ruleConditionOutput.getValue(),
              customerInput.getGender());
        }
      } else if (ConditionProperties.CURRENCY.equals(ruleConditionOutput.getProperties())) {
        if (!Objects.equals(ruleConditionOutput.getValue(), transactionInput.getCurrency())) {
          result = false;
          LOGGER.warn(
              "Rule: {} condition properties: {} operator: {} value: {} not match input: {}",
              rule.getCode(),
              ruleConditionOutput.getProperties(),
              ruleConditionOutput.getOperators(),
              ruleConditionOutput.getValue(),
              transactionInput.getCurrency());
        }
      } else if (ConditionProperties.DOB.equals(ruleConditionOutput.getProperties())) {
        if (customerInput.getDob() != null
            && !customerInput.getDob().isEqual(transactionInput.getTransactionAt().toLocalDate())) {
          result = false;
          LOGGER.warn(
              "Rule: {} condition properties: {} operator: {} value: {} not match input: {}",
              rule.getCode(),
              ruleConditionOutput.getProperties(),
              ruleConditionOutput.getOperators(),
              ruleConditionOutput.getValue(),
              customerInput.getDob());
        }
      } else if (ConditionProperties.CUSTOMER_TYPE.equals(ruleConditionOutput.getProperties())) {
        if (!Objects.equals(ruleConditionOutput.getValue(), customerInput.getCustomerType())) {
          result = false;
          LOGGER.warn(
              "Rule: {} condition properties: {} operator: {} value: {} not match input: {}",
              rule.getCode(),
              ruleConditionOutput.getProperties(),
              ruleConditionOutput.getOperators(),
              ruleConditionOutput.getValue(),
              customerInput.getCustomerType());
        }
      } else if (ConditionProperties.PRODUCT_TYPE.equals(ruleConditionOutput.getProperties())) {
        List<String> values =
            JsonUtils.fromJson(ruleConditionOutput.getValue(), List.class, String.class);
        if (transactionInput.getProductType() == null
            || (Operators.IN.equals(ruleConditionOutput.getOperators())
                && !values.contains(transactionInput.getProductType()))
            || (Operators.NOT_IN.equals(ruleConditionOutput.getOperators())
                && values.contains(transactionInput.getProductType()))) {
          result = false;
          LOGGER.warn(
              "Rule: {} condition properties: {} operator: {} value: {} not match input: {}",
              rule.getCode(),
              ruleConditionOutput.getProperties(),
              ruleConditionOutput.getOperators(),
              ruleConditionOutput.getValue(),
              transactionInput.getProductType());
        }
      } else if (ConditionProperties.PRODUCT_LINE.equals(ruleConditionOutput.getProperties())) {
        List<String> values =
            JsonUtils.fromJson(ruleConditionOutput.getValue(), List.class, String.class);
        if (transactionInput.getProductLine() == null
            || (Operators.IN.equals(ruleConditionOutput.getOperators())
                && !values.contains(transactionInput.getProductLine()))
            || (Operators.NOT_IN.equals(ruleConditionOutput.getOperators())
                && values.contains(transactionInput.getProductLine()))) {
          result = false;
          LOGGER.warn(
              "Rule: {} condition properties: {} operator: {} value: {} not match input: {}",
              rule.getCode(),
              ruleConditionOutput.getProperties(),
              ruleConditionOutput.getOperators(),
              ruleConditionOutput.getValue(),
              transactionInput.getProductLine());
        }
      } else if (ConditionProperties.TRANSACTION_GROUP.equals(
          ruleConditionOutput.getProperties())) {
        List<String> values =
            JsonUtils.fromJson(ruleConditionOutput.getValue(), List.class, String.class);
        if (transactionInput.getTransactionGroup() == null
            || (Operators.IN.equals(ruleConditionOutput.getOperators())
                && !values.contains(transactionInput.getTransactionGroup()))
            || (Operators.NOT_IN.equals(ruleConditionOutput.getOperators())
                && values.contains(transactionInput.getTransactionGroup()))) {
          result = false;
          LOGGER.warn(
              "Rule: {} condition properties: {} operator: {} value: {} not match input: {}",
              rule.getCode(),
              ruleConditionOutput.getProperties(),
              ruleConditionOutput.getOperators(),
              ruleConditionOutput.getValue(),
              transactionInput.getTransactionGroup());
        }
      } else if (ConditionProperties.TRANSACTION_TYPE.equals(ruleConditionOutput.getProperties())) {
        List<String> values =
            JsonUtils.fromJson(ruleConditionOutput.getValue(), List.class, String.class);
        if (transactionInput.getTransactionType() == null
            || (Operators.IN.equals(ruleConditionOutput.getOperators())
                && !values.contains(transactionInput.getTransactionType()))
            || (Operators.NOT_IN.equals(ruleConditionOutput.getOperators())
                && values.contains(transactionInput.getTransactionType()))) {
          result = false;
          LOGGER.warn(
              "Rule: {} condition properties: {} operator: {} value: {} not match input: {}",
              rule.getCode(),
              ruleConditionOutput.getProperties(),
              ruleConditionOutput.getOperators(),
              ruleConditionOutput.getValue(),
              transactionInput.getTransactionType());
        }
      } else if (ConditionProperties.CUSTOMER_AGE.equals(ruleConditionOutput.getProperties())) {
        List<Integer> values =
            JsonUtils.fromJson(ruleConditionOutput.getValue(), List.class, Integer.class);
        if (values.size() != 2) {
          LOGGER.warn(
              "Rule: {} condition properties: {} operator: {} value: {} not enough",
              rule.getCode(),
              ruleConditionOutput.getProperties(),
              ruleConditionOutput.getOperators(),
              ruleConditionOutput.getValue());
        } else {
          if (customerInput.getDob() != null) {
            var age = Period.between(customerInput.getDob(), LocalDate.now()).getYears();
            if (age < values.get(0) || age > values.get(1)) {
              result = false;
              LOGGER.warn(
                  "Rule: {} condition properties: {} operator: {} value: {} not match input: {}",
                  rule.getCode(),
                  ruleConditionOutput.getProperties(),
                  ruleConditionOutput.getOperators(),
                  ruleConditionOutput.getValue(),
                  customerInput.getDob());
            }
          } else {
            result = false;
            LOGGER.warn(
                "Rule: {} condition properties: {} operator: {} value: {} not match input: {}",
                rule.getCode(),
                ruleConditionOutput.getProperties(),
                ruleConditionOutput.getOperators(),
                ruleConditionOutput.getValue(),
                null);
          }
        }
      } else {
        LOGGER.warn("Rule condition: {} not support", ruleConditionOutput.getProperties());
      }
    }
    return result;
  }

  private LocalDateTime getStartDateCountTransaction(LocalDateTime startDate, Frequency frequency) {
    // tính thời gian bắt đầu dựa trên tần suất
    startDate =
        switch (frequency) {
          case MINUTE -> startDate.minusMinutes(1);
          case HOURS -> startDate.minusHours(1);
          case DAY -> startDate.minusDays(1);
          case WEEK -> startDate.minusWeeks(1);
          case MONTH -> startDate.minusMonths(1);
          case YEAR -> startDate.minusYears(1);
        };
    return startDate;
  }

  private BigInteger calculatorBonus(
      List<RuleBonusOutput> ruleBonusOutputs,
      BigInteger basePoint,
      CustomerOutput customerInput,
      AllocationPointTransactionInput transactionInput,
      LocalDate transactionDate) {
    BigDecimal bonusPoint = BigDecimal.ZERO;
    for (RuleBonusOutput ruleBonusOutput : ruleBonusOutputs) {
      BigDecimal value = BigDecimal.valueOf(ruleBonusOutput.getValue());
      // thưởng thêm theo hạng
      BigDecimal calculatorWithPercentage =
          value
              .multiply(new BigDecimal(basePoint))
              .divide(BigDecimal.valueOf(100L), 0, RoundingMode.HALF_UP);
      if (BonusType.BONUS_RANK.equals(ruleBonusOutput.getType())
          && ruleBonusOutput.getCondition().equals(customerInput.getRank())) {
        if (PlusType.FIX.equals(ruleBonusOutput.getPlusType())) {
          bonusPoint = bonusPoint.add(value);
        }
        if (PlusType.PERCENTAGE.equals(ruleBonusOutput.getPlusType())) {
          bonusPoint = bonusPoint.add(calculatorWithPercentage);
        }
      }
      // thưởng thêm với giao dịch thực hiện vào ngày sinh nhật
      if (BonusType.BONUS_DOB.equals(ruleBonusOutput.getType())
          && customerInput.getDob() != null
          && customerInput.getDob().isEqual(transactionDate)) {
        if (PlusType.FIX.equals(ruleBonusOutput.getPlusType())) {
          bonusPoint = bonusPoint.add(value);
        }
        if (PlusType.PERCENTAGE.equals(ruleBonusOutput.getPlusType())) {
          bonusPoint = bonusPoint.add(calculatorWithPercentage);
        }
      }
      // thưởng thêm giao dịch ngày đặc biệt
      if (BonusType.BONUS_SPECIAL_DATE.equals(ruleBonusOutput.getType())) {
        LocalDate startDate = Utils.convertToLocalDate(ruleBonusOutput.getCondition());
        LocalDate endDate = Utils.convertToLocalDate(ruleBonusOutput.getChildCondition());
        if (startDate != null
            && endDate != null
            && (transactionDate.isEqual(startDate)
                || transactionDate.isEqual(endDate)
                || transactionDate.isAfter(startDate)
                || transactionDate.isBefore(startDate))) {
          if (PlusType.FIX.equals(ruleBonusOutput.getPlusType())) {
            bonusPoint = bonusPoint.add(value);
          }
          if (PlusType.PERCENTAGE.equals(ruleBonusOutput.getPlusType())) {
            bonusPoint = bonusPoint.add(calculatorWithPercentage);
          }
        }
      }
      // thưởng thêm theo dòng sản phẩm/dịch vụ
      if (BonusType.BONUS_PRODUCT.equals(ruleBonusOutput.getType())
          && ruleBonusOutput.getCondition().equals(transactionInput.getProductType())
          && (StringUtils.isBlank(ruleBonusOutput.getChildCondition())
              || ruleBonusOutput.getChildCondition().equals(transactionInput.getProductLine()))) {
        if (PlusType.FIX.equals(ruleBonusOutput.getPlusType())) {
          bonusPoint = bonusPoint.add(value);
        }
        if (PlusType.PERCENTAGE.equals(ruleBonusOutput.getPlusType())) {
          bonusPoint = bonusPoint.add(calculatorWithPercentage);
        }
      }
      // thưởng thêm với chi tiêu vượt ngưỡng
      if (BonusType.BONUS_EXCEED_THRESHOLD.equals(ruleBonusOutput.getType())
          && StringUtils.isNotBlank(ruleBonusOutput.getCondition())
          && Long.parseLong(ruleBonusOutput.getCondition()) > transactionInput.getAmount()) {
        if (PlusType.FIX.equals(ruleBonusOutput.getPlusType())) {
          bonusPoint = bonusPoint.add(value);
        }
        if (PlusType.PERCENTAGE.equals(ruleBonusOutput.getPlusType())) {
          bonusPoint = bonusPoint.add(calculatorWithPercentage);
        }
      }
      // thưởng thêm giao dịch đầu tiên
      if (BonusType.BONUS_FIRST_TRANSACTION.equals(ruleBonusOutput.getType())) {
        var customerBalance =
            customerBalanceRepository.findByDeletedFalseAndId(customerInput.getId()).orElse(null);
        if (customerBalance != null && customerBalance.getTotalAccumulatedPoints() == 0) {
          if (PlusType.FIX.equals(ruleBonusOutput.getPlusType())) {
            bonusPoint = bonusPoint.add(value);
          }
          if (PlusType.PERCENTAGE.equals(ruleBonusOutput.getPlusType())) {
            bonusPoint = bonusPoint.add(calculatorWithPercentage);
          }
        }
      }
    }

    return bonusPoint.toBigInteger();
  }
}
