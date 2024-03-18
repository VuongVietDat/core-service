package vn.com.atomi.loyalty.core.service;

import org.springframework.data.domain.Pageable;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.core.dto.input.TransactionInput;
import vn.com.atomi.loyalty.core.dto.output.CustomerBalanceOutput;
import vn.com.atomi.loyalty.core.dto.output.ExternalCustomerBalanceHistoryOutput;
import vn.com.atomi.loyalty.core.enums.ChangeType;

/**
 * @author haidv
 * @version 1.0
 */
public interface CustomerBalanceService {

  CustomerBalanceOutput getCurrentBalance(String cifBank, String cifWallet);

  ResponsePage<ExternalCustomerBalanceHistoryOutput> getBalanceHistories(
      Long customerId, ChangeType changeType, String startDate, String endDate, Pageable pageable);

  void executeTransactionMinus(TransactionInput transactionInput);
}
