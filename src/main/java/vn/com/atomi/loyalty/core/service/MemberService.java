package vn.com.atomi.loyalty.core.service;

import org.springframework.data.domain.Pageable;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.core.dto.output.CustomerOutput;
import vn.com.atomi.loyalty.core.enums.Status;

import java.util.LinkedHashMap;
import java.util.List;

public interface MemberService {
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
