package vn.com.atomi.loyalty.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.core.dto.output.CustomerBalanceOutput;
import vn.com.atomi.loyalty.core.dto.projection.CustomerBalanceProjection;
import vn.com.atomi.loyalty.core.enums.ErrorCode;
import vn.com.atomi.loyalty.core.repository.CustomerBalanceRepository;
import vn.com.atomi.loyalty.core.service.CustomerBalanceService;
import vn.com.atomi.loyalty.core.utils.Constants;

/**
 * @author haidv
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class CustomerBalanceServiceImpl extends BaseService implements CustomerBalanceService {

  private final CustomerBalanceRepository customerBalanceRepository;

  @Override
  public CustomerBalanceOutput getCurrentBalance(String cifBank, String cifWallet) {
    if (StringUtils.isBlank(cifBank) && StringUtils.isBlank(cifBank)) {
      throw new BaseException(ErrorCode.INPUT_INVALID);
    }
    CustomerBalanceProjection balanceProjection =
        customerBalanceRepository.findCurrentBalance(cifBank, cifWallet);
    if (balanceProjection == null || balanceProjection.getCustomerId() == null) {
      return CustomerBalanceOutput.builder()
          .availableAmount(0L)
          .lockAmount(0L)
          .availableAmount(0L)
          .cifBank(cifBank)
          .cifWallet(cifWallet)
          .note(Constants.NOTE_CUSTOMER_NOT_FOUND)
          .build();
    }
    return null;
  }
}
