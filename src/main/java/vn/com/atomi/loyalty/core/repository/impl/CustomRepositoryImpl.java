package vn.com.atomi.loyalty.core.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.base.exception.CommonErrorCode;
import vn.com.atomi.loyalty.core.dto.input.CustomerKafkaInput;
import vn.com.atomi.loyalty.core.entity.CustomerBalance;
import vn.com.atomi.loyalty.core.entity.CustomerRank;
import vn.com.atomi.loyalty.core.enums.ChangeType;
import vn.com.atomi.loyalty.core.enums.ErrorCode;
import vn.com.atomi.loyalty.core.enums.PointType;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.repository.CustomRepository;
import vn.com.atomi.loyalty.core.utils.Utils;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
@RequiredArgsConstructor
public class CustomRepositoryImpl implements CustomRepository {

  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  private final EntityManager entityManager;

  @Override
  public Long plusAmount(
      Long customerId,
      Long amount,
      String refNo,
      String campaignCode,
      Long campaignId,
      String ruleCode,
      Long ruleId,
      LocalDateTime transactionAt,
      String content,
      LocalDate expireAt,
      PointType pointType) {
    StoredProcedureQuery query =
        entityManager
            .createStoredProcedureQuery("P_PLUS_AMOUNT")
            .registerStoredProcedureParameter("P_CUSTOMER_ID", Long.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_AMOUNT", Long.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_REF_NO", String.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_CAMPAIGN_CODE", String.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_CAMPAIGN_ID", Long.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_RULE_CODE", String.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_RULE_ID", Long.class, ParameterMode.IN)
            .registerStoredProcedureParameter(
                "P_TRANSACTION_AT", LocalDateTime.class, ParameterMode.IN)
            .registerStoredProcedureParameter(
                "P_SEARCH_TRANSACTION_DATE", String.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_CONTENT", String.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_EXPIRE_AT", LocalDate.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_POINT_TYPE", String.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_CHANGE_TYPE", String.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_TRANSACTION_ID", Long.class, ParameterMode.OUT)
            .registerStoredProcedureParameter("P_RESULT", Long.class, ParameterMode.OUT)
            .registerStoredProcedureParameter("P_RESULT_DESC", String.class, ParameterMode.OUT)
            .setParameter("P_CUSTOMER_ID", customerId)
            .setParameter("P_AMOUNT", amount)
            .setParameter("P_REF_NO", refNo)
            .setParameter("P_CAMPAIGN_CODE", campaignCode)
            .setParameter("P_CAMPAIGN_ID", campaignId)
            .setParameter("P_RULE_CODE", ruleCode)
            .setParameter("P_RULE_ID", ruleId)
            .setParameter("P_TRANSACTION_AT", transactionAt)
            .setParameter(
                "P_SEARCH_TRANSACTION_DATE",
                DateTimeFormatter.ofPattern(DateConstant.ISO_8601_EXTENDED_DATE_FORMAT_STROKE)
                    .format(transactionAt))
            .setParameter("P_CONTENT", content)
            .setParameter("P_EXPIRE_AT", expireAt)
            .setParameter("P_POINT_TYPE", pointType.name())
            .setParameter("P_CHANGE_TYPE", ChangeType.PLUS.name());
    query.execute();
    Long result = (Long) query.getOutputParameterValue("P_RESULT");
    String resultDesc = (String) query.getOutputParameterValue("P_RESULT_DESC");
    LOGGER.info("CustomRepository.plusAmount execute result : {} {}", result, resultDesc);
    if (result != 0) {
      throw new BaseException(CommonErrorCode.DATA_INTEGRITY_VIOLATION);
    }
    return (Long) query.getOutputParameterValue("P_TRANSACTION_ID");
  }

  public Long minusAmount(
      Long customerId,
      Long amount,
      String refNo,
      LocalDateTime transactionAt,
      String content,
      PointType pointType) {
    StoredProcedureQuery query =
        entityManager
            .createStoredProcedureQuery("P_MINUS_AMOUNT")
            .registerStoredProcedureParameter("P_CUSTOMER_ID", Long.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_AMOUNT", Long.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_REF_NO", String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(
                "P_TRANSACTION_AT", LocalDateTime.class, ParameterMode.IN)
            .registerStoredProcedureParameter(
                "P_SEARCH_TRANSACTION_DATE", String.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_CONTENT", String.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_POINT_TYPE", String.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_CHANGE_TYPE", String.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_TRANSACTION_ID", Long.class, ParameterMode.OUT)
            .registerStoredProcedureParameter("P_RESULT", Long.class, ParameterMode.OUT)
            .registerStoredProcedureParameter("P_RESULT_DESC", String.class, ParameterMode.OUT)
            .setParameter("P_CUSTOMER_ID", customerId)
            .setParameter("P_AMOUNT", amount)
            .setParameter("P_REF_NO", refNo)
            .setParameter("P_TRANSACTION_AT", transactionAt)
            .setParameter(
                "P_SEARCH_TRANSACTION_DATE",
                DateTimeFormatter.ofPattern(DateConstant.ISO_8601_EXTENDED_DATE_FORMAT_STROKE)
                    .format(transactionAt))
            .setParameter("P_CONTENT", content)
            .setParameter("P_POINT_TYPE", pointType.name())
            .setParameter("P_CHANGE_TYPE", ChangeType.MINUS_CONSUMPTION.name());
    query.execute();
    Long result = (Long) query.getOutputParameterValue("P_RESULT");
    String resultDesc = (String) query.getOutputParameterValue("P_RESULT_DESC");
    LOGGER.info("CustomRepository.plusAmount execute result : {} {}", result, resultDesc);
    if (result == -1) {
      throw new BaseException(ErrorCode.NOT_ENOUGH_BALANCE);
    }
    if (result != 0) {
      throw new BaseException(CommonErrorCode.DATA_INTEGRITY_VIOLATION);
    }
    return (Long) query.getOutputParameterValue("P_TRANSACTION_ID");
  }

  public void expiredAmount(
      String refNo, LocalDate expiredAt, String content, PointType pointType) {
    LocalDateTime transactionAt = LocalDateTime.now();
    StoredProcedureQuery query =
        entityManager
            .createStoredProcedureQuery("P_EXPIRED_AMOUNT")
            .registerStoredProcedureParameter("P_REF_NO", String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(
                "P_TRANSACTION_AT", LocalDateTime.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_EXPIRED_AT", LocalDate.class, ParameterMode.IN)
            .registerStoredProcedureParameter(
                "P_SEARCH_TRANSACTION_DATE", String.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_CONTENT", String.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_POINT_TYPE", String.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_CHANGE_TYPE", String.class, ParameterMode.IN)
            .registerStoredProcedureParameter("P_RESULT", Long.class, ParameterMode.OUT)
            .registerStoredProcedureParameter("P_RESULT_DESC", String.class, ParameterMode.OUT)
            .setParameter("P_REF_NO", refNo)
            .setParameter("P_TRANSACTION_AT", transactionAt)
            .setParameter("P_EXPIRED_AT", expiredAt)
            .setParameter(
                "P_SEARCH_TRANSACTION_DATE",
                DateTimeFormatter.ofPattern(DateConstant.ISO_8601_EXTENDED_DATE_FORMAT_STROKE)
                    .format(transactionAt))
            .setParameter("P_CONTENT", content)
            .setParameter("P_POINT_TYPE", pointType.name())
            .setParameter("P_CHANGE_TYPE", ChangeType.MINUS_CONSUMPTION.name());
    query.execute();
    Long result = (Long) query.getOutputParameterValue("P_RESULT");
    String resultDesc = (String) query.getOutputParameterValue("P_RESULT_DESC");
    LOGGER.info("CustomRepository.expiredAmount execute result : {} {}", result, resultDesc);
    if (result != 0) {
      throw new BaseException(CommonErrorCode.DATA_INTEGRITY_VIOLATION);
    }
  }

  @Override
  public void saveAllCustomer(
      List<Triple<CustomerKafkaInput, CustomerBalance, CustomerRank>> infos) {
    var currentTime = Utils.formatLocalDateToString(LocalDate.now());
    var creator = "SYSTEM";

    var values =
        infos.stream()
            .map(
                info -> {
                  var customer = info.getLeft();
                  var cb = info.getMiddle();
                  var cr = info.getRight();

                  var intoCus =
                      String.format(
                          """
                          INTO C_CUSTOMER (ID, CIF_BANK, CIF_WALLET, CUSTOMER_NAME, DOB, CURRENT_ADDRESS, CUSTOMER_TYPE,
                          GENDER, NATIONALITY, OWNER_BRANCH, PHONE, RANK, REGISTER_BRANCH, RESIDENTIAL_ADDRESS, RM_CODE,
                          RM_NAME, SEGMENT, UNIQUE_TYPE, UNIQUE_VALUE, ISSUE_DATE, ISSUE_PLACE, STATUS, CREATED_AT, CREATED_BY, UPDATED_AT, UPDATED_BY, IS_DELETED)
                          VALUES (GET_C_CUSTOMER_ID_SEQ(), '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s',
                          '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %d)
                          """,
                          customer.getCifBank(),
                          customer.getCifWallet(),
                          customer.getCustomerName(),
                          customer.getDob(),
                          customer.getCurrentAddress(),
                          customer.getCustomerType(),
                          customer.getGender(),
                          customer.getNationality(),
                          customer.getOwnerBranch(),
                          customer.getPhone(),
                          customer.getRank(),
                          customer.getRegisterBranch(),
                          customer.getResidentialAddress(),
                          customer.getRmCode(),
                          customer.getRmName(),
                          customer.getSegment(),
                          customer.getUniqueType(),
                          customer.getUniqueValue(),
                          customer.getIssueDate(),
                          customer.getIssuePlace(),
                          Status.ACTIVE.name(),
                          currentTime,
                          creator,
                          currentTime,
                          creator,
                          0);

                  var intoCusBal =
                      String.format(
                          """
                          INTO C_CUSTOMER_BALANCE (ID, CODE, CUSTOMER_ID, TOTAL_AMOUNT, LOCK_AMOUNT, AVAILABLE_AMOUNT,
                          TOTAL_POINTS_USED, TOTAL_ACCUMULATED_POINTS, TOTAL_POINTS_EXPIRED, STATUS, CREATED_AT,
                          CREATED_BY, UPDATED_AT, UPDATED_BY, IS_DELETED)
                          VALUES (GET_C_CUSTOMER_BALANCE_ID_SEQ(), '%s', %d, %d, %d, %d, %d, %d, %d, '%s', '%s', '%s', '%s', '%s', %d)
                          """,
                          cb.getCode(),
                          cb.getCustomerId(),
                          cb.getTotalAmount(),
                          cb.getLockAmount(),
                          cb.getAvailableAmount(),
                          cb.getTotalPointsUsed(),
                          cb.getTotalAccumulatedPoints(),
                          cb.getTotalPointsExpired(),
                          Status.ACTIVE.name(),
                          currentTime,
                          creator,
                          currentTime,
                          creator,
                          0);

                  var intoCusRank =
                      String.format(
                          """
                          INTO C_CUSTOMER_RANK (ID, CODE, CUSTOMER_ID, RANK, APPLY_DATE, TOTAL_POINT, STATUS,
                          CREATED_AT, CREATED_BY, UPDATED_AT, UPDATED_BY, IS_DELETED)
                          VALUES (GET_C_CUSTOMER_RANK_ID_SEQ(), '%s', %d, '%s', '%s', %d, '%s', '%s', '%s', '%s', '%s', %d)
                          """,
                          cr.getCode(),
                          cr.getCustomerId(),
                          cr.getRank(),
                          Utils.formatLocalDateToString(cr.getApplyDate()),
                          cr.getTotalPoint(),
                          Status.ACTIVE.name(),
                          currentTime,
                          creator,
                          currentTime,
                          creator,
                          0);

                  return intoCus + '\n' + intoCusBal + '\n' + intoCusRank;
                })
            .collect(Collectors.joining("\n"))
            .replace("'null'", "null");

    entityManager
        .createNativeQuery("INSERT ALL\n" + values + "\nSELECT * FROM DUAL")
        .executeUpdate();
  }
}
