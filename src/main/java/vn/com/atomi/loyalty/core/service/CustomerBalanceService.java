package vn.com.atomi.loyalty.core.service;

import vn.com.atomi.loyalty.core.dto.output.CustomerBalanceOutput;

/**
 * @author haidv
 * @version 1.0
 */
public interface CustomerBalanceService {

  CustomerBalanceOutput getCurrentBalance(String cifBank, String cifWallet);
}
