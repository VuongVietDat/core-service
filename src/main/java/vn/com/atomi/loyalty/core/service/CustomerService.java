package vn.com.atomi.loyalty.core.service;

import org.springframework.data.domain.Pageable;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.core.dto.output.CustomerOutput;
import vn.com.atomi.loyalty.core.dto.output.CustomerPointAccountOutput;
import vn.com.atomi.loyalty.core.dto.output.CustomerPointAccountPreviewOutput;
import vn.com.atomi.loyalty.core.enums.Status;

import java.util.LinkedHashMap;
import java.util.List;

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
      Pageable pageable);

  ResponsePage<CustomerOutput> gets(
      Status status,
      Long customerId,
      String customerName,
      String cifBank,
      String rank,
      String segment,
      Pageable pageable);

  CustomerOutput get(Long id);

  void creates(String messageId, List<LinkedHashMap> input);
}
