package vn.com.atomi.loyalty.core.service;

import org.springframework.data.domain.Pageable;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.core.dto.input.TransactionInput;
import vn.com.atomi.loyalty.core.dto.output.CustomerBalanceHistoryOutput;
import vn.com.atomi.loyalty.core.dto.output.CustomerBalanceOutput;
import vn.com.atomi.loyalty.core.dto.output.ExternalCustomerBalanceHistoryOutput;
import vn.com.atomi.loyalty.core.enums.ChangeType;
import vn.com.atomi.loyalty.core.enums.PointType;

/**
 * @author haidv
 * @version 1.0
 */
public interface CustomerBalanceService {

  CustomerBalanceOutput getCurrentBalance(String cifBank, String cifWallet);

  ResponsePage<ExternalCustomerBalanceHistoryOutput> getBalanceHistories(
      Long customerId,
      ChangeType changeType,
      String startTransactionDate,
      String endTransactionDate,
      Pageable pageable);

  ResponsePage<CustomerBalanceHistoryOutput> getBalanceHistories(
      Long customerId,
      ChangeType changeType,
      PointType pointType,
      String startTransactionDate,
      String endTransactionDate,
      String startExpiredDate,
      String endExpiredDate,
      Pageable pageable);

  void executeTransactionMinus(TransactionInput transactionInput);

  void executePointExpiration();
}
