package vn.com.atomi.loyalty.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.base.exception.CommonErrorCode;
import vn.com.atomi.loyalty.core.dto.output.CustomerOutput;
import vn.com.atomi.loyalty.core.dto.output.RankOutput;
import vn.com.atomi.loyalty.core.entity.Customer;
import vn.com.atomi.loyalty.core.entity.CustomerBalance;
import vn.com.atomi.loyalty.core.entity.CustomerRank;
import vn.com.atomi.loyalty.core.enums.ErrorCode;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.feign.LoyaltyConfigClient;
import vn.com.atomi.loyalty.core.repository.CustomRepository;
import vn.com.atomi.loyalty.core.repository.CustomerRepository;
import vn.com.atomi.loyalty.core.service.MemberService;
import vn.com.atomi.loyalty.core.utils.Utils;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl extends BaseService implements MemberService {
  private final LoyaltyConfigClient configClient;
  private final CustomRepository customRepository;
  private final CustomerRepository customerRepository;
  private final ObjectMapper mapper;

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

  @Transactional
  @Override
  public void creates(String requestId, List<LinkedHashMap> input) {
    var rankCode = getFirstRank(requestId);
    var currentDate = LocalDate.now();

    // init data
    var list = new ArrayList<Triple<Customer, CustomerBalance, CustomerRank>>();

    var sequence = customerRepository.getSequence();
    var n = input.size();

    for (int i = 0; i < n; i++) {
      var map = input.get(i);
      var cus = mapper.convertValue(map, Customer.class);

      var cusId = sequence + i + 1;

      var cb =
          CustomerBalance.builder()
              .customerId(cusId)
              .code(Utils.generateCode(cusId, CustomerBalance.class.getSimpleName()))
              .totalAmount(0L)
              .lockAmount(0L)
              .availableAmount(0L)
              .totalPointsUsed(0L)
              .totalAccumulatedPoints(0L)
              .totalPointsExpired(0L)
              .status(Status.ACTIVE)
              .build();

      var cr =
          CustomerRank.builder()
              .customerId(cusId)
              .code(Utils.generateCode(cusId, CustomerRank.class.getSimpleName()))
              .rank(rankCode)
              .applyDate(currentDate)
              .totalPoint(0L)
              .status(Status.ACTIVE)
              .build();

      list.add(Triple.of(cus, cb, cr));
    }

    // saves
    customRepository.saveAllCustomer(list);
  }

  private String getFirstRank(String requestId) {
    var res = configClient.getAllRanks(requestId);
    if (res.getCode() != 0) throw new BaseException(CommonErrorCode.EXECUTE_THIRTY_SERVICE_ERROR);
    var ranks = res.getData();
    return ranks.stream()
        .min(Comparator.comparing(RankOutput::getOrderNo))
        .orElseThrow(() -> new BaseException(CommonErrorCode.ENTITY_NOT_FOUND))
        .getCode();
  }
}
