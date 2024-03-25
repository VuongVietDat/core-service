package vn.com.atomi.loyalty.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
import vn.com.atomi.loyalty.core.repository.CustomerBalanceRepository;
import vn.com.atomi.loyalty.core.repository.CustomerRankRepository;
import vn.com.atomi.loyalty.core.repository.CustomerRepository;
import vn.com.atomi.loyalty.core.service.MemberService;
import vn.com.atomi.loyalty.core.utils.Utils;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl extends BaseService implements MemberService {
  private final LoyaltyConfigClient configClient;
  private final CustomerRepository customerRepository;
  private final CustomerBalanceRepository customerBalanceRepository;
  private final CustomerRankRepository customerRankRepository;
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
    var currentDate = LocalDate.now();
    var rankCode = getFirstRank(requestId);

    // init data
    var listCustomer = new ArrayList<Customer>();
    var listCustomerBalances = new ArrayList<CustomerBalance>();
    var listCustomerRanks = new ArrayList<CustomerRank>();

    input.forEach(
        map -> {
          // lay cac next ID
          var cusId = customerRepository.getSequence();
          var cusBalanceId = customerBalanceRepository.getSequence();
          var cusRankId = customerRankRepository.getSequence();

          var cus = mapper.convertValue(map, Customer.class);
          cus.setId(cusId);
          listCustomer.add(cus);

          var cbCode = Utils.generateCode(cusBalanceId, CustomerBalance.class.getSimpleName());
          listCustomerBalances.add(
              CustomerBalance.builder()
                  .id(cusBalanceId)
                  .customerId(cusId)
                  .code(cbCode)
                  .totalAmount(0L)
                  .lockAmount(0L)
                  .availableAmount(0L)
                  .totalPointsUsed(0L)
                  .totalAccumulatedPoints(0L)
                  .totalPointsExpired(0L)
                  .status(Status.ACTIVE)
                  .build());

          var crCode = Utils.generateCode(cusRankId, CustomerRank.class.getSimpleName());
          listCustomerRanks.add(
              CustomerRank.builder()
                  .id(cusRankId)
                  .customerId(cusId)
                  .code(crCode)
                  .rank(rankCode)
                  .applyDate(currentDate)
                  .totalPoint(0L)
                  .status(Status.ACTIVE)
                  .build());
        });

    // saves
    customerRepository.saveAll(listCustomer);
    customerBalanceRepository.saveAll(listCustomerBalances);
    customerRankRepository.saveAll(listCustomerRanks);
  }

  private String getFirstRank(String requestId) {
    var res = configClient.getAllRanks(requestId);
    if (res.getCode() != 0) throw new BaseException(CommonErrorCode.EXECUTE_THIRTY_SERVICE_ERROR);
    var ranks = res.getData();
    return ranks.stream()
        .min(Comparator.comparing(RankOutput::getId))
        .orElseThrow(() -> new BaseException(CommonErrorCode.ENTITY_NOT_FOUND))
        .getCode();
  }
}
