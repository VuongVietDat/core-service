package vn.com.atomi.loyalty.core.service;

import org.springframework.data.domain.Pageable;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.core.dto.output.CustomerPointAccountOutput;
import vn.com.atomi.loyalty.core.dto.output.CustomerPointAccountPreviewOutput;
import vn.com.atomi.loyalty.core.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
public interface CustomerService {

  CustomerPointAccountOutput getCustomerPointAccount(Long id);

  ResponsePage<CustomerPointAccountPreviewOutput> getCustomerPointAccounts(
      Status status,
      Long customerId,
      String customerName,
      String cifBank,
      String cifWallet,
      String uniqueValue,
      Long pointFrom,
      Long pointTo,
      String rank,
      Pageable pageable);
}
