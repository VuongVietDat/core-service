package vn.com.atomi.loyalty.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.core.dto.input.TransactionInput;
import vn.com.atomi.loyalty.core.dto.output.CustomerBalanceOutput;
import vn.com.atomi.loyalty.core.dto.output.ExternalCustomerBalanceHistoryOutput;
import vn.com.atomi.loyalty.core.dto.projection.CustomerBalanceProjection;
import vn.com.atomi.loyalty.core.enums.ChangeType;
import vn.com.atomi.loyalty.core.enums.ErrorCode;
import vn.com.atomi.loyalty.core.repository.CustomerBalanceHistoryRepository;
import vn.com.atomi.loyalty.core.repository.CustomerBalanceRepository;
import vn.com.atomi.loyalty.core.service.CustomerBalanceService;

/**
 * @author haidv
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class CustomerBalanceServiceImpl extends BaseService implements CustomerBalanceService {

  private final CustomerBalanceRepository customerBalanceRepository;

  private final CustomerBalanceHistoryRepository customerBalanceHistoryRepository;

  @Override
  public CustomerBalanceOutput getCurrentBalance(String cifBank, String cifWallet) {
    if (StringUtils.isBlank(cifBank) && StringUtils.isBlank(cifBank)) {
      throw new BaseException(ErrorCode.INPUT_INVALID);
    }
    CustomerBalanceProjection balanceProjection =
        customerBalanceRepository.findCurrentBalance(cifBank, cifWallet);
    if (balanceProjection == null || balanceProjection.getCustomerId() == null) {
      throw new BaseException(ErrorCode.CUSTOMER_NOT_EXISTED);
    }
    return super.modelMapper.convertToCustomerBalanceOutput(balanceProjection);
  }

  @Override
  public ResponsePage<ExternalCustomerBalanceHistoryOutput> getBalanceHistories(
      Long customerId, ChangeType changeType, String startDate, String endDate, Pageable pageable) {
    String searchDate = "searchDate";
    if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
      searchDate = null;
    }
    var page =
        customerBalanceHistoryRepository.findHistory(
            customerId, changeType, searchDate, startDate, endDate, pageable);
    return new ResponsePage<>(
        page, super.modelMapper.convertToCustomerBalanceHistoryOutputs(page.getContent()));
  }

  @Override
  public void executeTransactionMinus(TransactionInput transactionInput) {}
}
