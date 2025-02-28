package vn.com.atomi.loyalty.core.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.commons.lang3.tuple.Triple;
import vn.com.atomi.loyalty.core.dto.input.CustomerKafkaInput;
import vn.com.atomi.loyalty.core.dto.input.TransactionInput;
import vn.com.atomi.loyalty.core.entity.CustomerBalance;
import vn.com.atomi.loyalty.core.entity.CustomerRank;
import vn.com.atomi.loyalty.core.enums.PointType;

/**
 * @author haidv
 * @version 1.0
 */
public interface CustomRepository {

  Long plusAmount(TransactionInput transactionInput);

  List<Long> plusAmounts(List<TransactionInput> transactionInputs);

  Long minusAmount(
      Long customerId,
      Long amount,
      String refNo,
      LocalDateTime transactionAt,
      String content,
      PointType pointType);

  void expiredAmount(String refNo, LocalDate expiredAt, String content, PointType pointType);

  void saveAllCustomer(List<Triple<CustomerKafkaInput, CustomerBalance, CustomerRank>> infos);
}
