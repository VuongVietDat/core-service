package vn.com.atomi.loyalty.core.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.base.exception.CommonErrorCode;
import vn.com.atomi.loyalty.core.enums.ChangeType;
import vn.com.atomi.loyalty.core.enums.PointType;
import vn.com.atomi.loyalty.core.repository.CustomRepository;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
@RequiredArgsConstructor
public class CustomRepositoryImpl implements CustomRepository {

  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  private final EntityManager entityManager;

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
      PointType pointType,
      ChangeType changeType) {
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
            .setParameter("P_CHANGE_TYPE", changeType.name());
    query.execute();
    Long result = (Long) query.getOutputParameterValue("P_RESULT");
    if (result != 0) {
      LOGGER.error(
          "CustomRepository.plusAmount error : {}", query.getOutputParameterValue("P_RESULT_DESC"));
      throw new BaseException(CommonErrorCode.DATA_INTEGRITY_VIOLATION);
    }
    return (Long) query.getOutputParameterValue("P_TRANSACTION_ID");
  }
}
