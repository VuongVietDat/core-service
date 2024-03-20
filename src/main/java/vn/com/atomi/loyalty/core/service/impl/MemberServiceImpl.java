package vn.com.atomi.loyalty.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.core.dto.output.CustomerOutput;
import vn.com.atomi.loyalty.core.enums.ErrorCode;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.repository.CustomerRepository;
import vn.com.atomi.loyalty.core.service.MemberService;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl extends BaseService implements MemberService {
  private final CustomerRepository customerRepository;

  @Override
  public ResponsePage<CustomerOutput> gets(
      Status status,
      Long customerId,
      String customerName,
      String cifBank,
      String rank,
      String segment,
      Pageable pageable) {

    var page =
        customerRepository.findByCondition(
            status, customerId, customerName, cifBank, rank, segment, pageable);

    return new ResponsePage<>(page, modelMapper.convertToCustomerOutputs(page.getContent()));
  }

  @Override
  public CustomerOutput get(Long id) {
    return customerRepository
        .findById(id)
        .map(modelMapper::convertToCustomerOutput)
        .orElseThrow(() -> new BaseException(ErrorCode.CUSTOMER_NOT_EXISTED));
  }
}
