package vn.com.atomi.loyalty.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.base.exception.CommonErrorCode;
import vn.com.atomi.loyalty.core.dto.input.CustomerKafkaInput;
import vn.com.atomi.loyalty.core.dto.output.CustomerOutput;
import vn.com.atomi.loyalty.core.dto.output.CustomerPointAccountOutput;
import vn.com.atomi.loyalty.core.dto.output.CustomerPointAccountPreviewOutput;
import vn.com.atomi.loyalty.core.dto.output.RankOutput;
import vn.com.atomi.loyalty.core.entity.CustomerBalance;
import vn.com.atomi.loyalty.core.entity.CustomerRank;
import vn.com.atomi.loyalty.core.enums.ErrorCode;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.feign.LoyaltyConfigClient;
import vn.com.atomi.loyalty.core.repository.CustomRepository;
import vn.com.atomi.loyalty.core.repository.CustomerBalanceHistoryRepository;
import vn.com.atomi.loyalty.core.repository.CustomerRepository;
import vn.com.atomi.loyalty.core.service.CustomerService;
import vn.com.atomi.loyalty.core.service.MasterDataService;
import vn.com.atomi.loyalty.core.utils.Constants;
import vn.com.atomi.loyalty.core.utils.Utils;

/**
 * @author haidv
 * @version 1.0
 */
@SuppressWarnings({"rawtypes"})
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl extends BaseService implements CustomerService {

  private final CustomerRepository customerRepository;

  private final CustomerBalanceHistoryRepository customerBalanceHistoryRepository;

  private final MasterDataService masterDataService;
  private final LoyaltyConfigClient configClient;
  private final CustomRepository customRepository;
  private final ObjectMapper mapper;

  @Override
  public CustomerPointAccountOutput getCustomerPointAccount(Long customerId) {
    var pointAccountProjection = customerRepository.findByDeletedFalseAndPointAccount(customerId);
    if (pointAccountProjection == null || pointAccountProjection.getCustomerId() == null) {
      throw new BaseException(ErrorCode.CUSTOMER_NOT_EXISTED);
    }
    // lấy danh sách rank để map tên rank
    var uniqueType = masterDataService.getDictionary(Constants.DICTIONARY_UNIQUE_TYPE, false);
    var out =
        super.modelMapper.convertToCustomerPointAccountOutput(pointAccountProjection, uniqueType);
    // lấy điểm gần hết hạn
    customerBalanceHistoryRepository
        .findPointAboutExpire(customerId)
        .ifPresent(
            customerBalanceHistory -> {
              out.setPointAboutExpire(
                  customerBalanceHistory.getAmount() - customerBalanceHistory.getAmountUsed());
              out.setMostRecentExpirationDate(customerBalanceHistory.getExpireAt());
            });
    return out;
  }

  @Override
  public ResponsePage<CustomerPointAccountPreviewOutput> getCustomerPointAccounts(
      Status status,
      Long customerId,
      String customerName,
      String cifBank,
      String cifWallet,
      String uniqueValue,
      Long pointFrom,
      Long pointTo,
      Pageable pageable) {
    var pointAccountProjectionPage =
        customerRepository.findByDeletedFalseAndPointAccount(
            status,
            customerId,
            customerName,
            cifBank,
            cifWallet,
            uniqueValue,
            pointFrom,
            pointTo,
            pageable);
    if (!CollectionUtils.isEmpty(pointAccountProjectionPage.getContent())) {
      // lấy master data để map tên loại giấy tờ tùy thân
      var dictionaryOutputs =
          masterDataService.getDictionary(Constants.DICTIONARY_UNIQUE_TYPE, false);
      return new ResponsePage<>(
          pointAccountProjectionPage,
          super.modelMapper.convertToCustomerPointAccountPreviewOutputs(
              pointAccountProjectionPage.getContent(), dictionaryOutputs));
    }
    return new ResponsePage<>(pointAccountProjectionPage, new ArrayList<>());
  }

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
    var list = new ArrayList<Triple<CustomerKafkaInput, CustomerBalance, CustomerRank>>();

    var sequence = customerRepository.getSequence();
    var n = input.size();

    for (int i = 0; i < n; i++) {
      var map = input.get(i);
      var cus = mapper.convertValue(map, CustomerKafkaInput.class);

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

  @Override
  public void update(String messageId, LinkedHashMap input) {
    var cus = mapper.convertValue(input, CustomerKafkaInput.class);

    customerRepository
        .findByCifBank(cus.getCifBank())
        .ifPresent(
            customer -> customerRepository.save(modelMapper.fromCustomerKafkaInput(customer, cus)));
  }

  @Transactional
  @Override
  public void delete(String messageId, LinkedHashMap input) {
    var cus = mapper.convertValue(input, CustomerKafkaInput.class);
    var cif = cus.getCifBank();
    if (!StringUtils.isBlank(cif))
      customerRepository.findByCifBank(cif).ifPresent(customer -> customer.setDeleted(true));
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

  @Override
  public CustomerOutput getCustomer(String cifBank, String cifWallet) {
    // bắt buộc truyền 1 trong 2 param
    if (StringUtils.isBlank(cifBank) && StringUtils.isBlank(cifBank)) {
      throw new BaseException(ErrorCode.INPUT_INVALID);
    }
    return customerRepository
        .findByDeletedFalseAndCifWallet(cifWallet, cifBank)
        .map(modelMapper::convertToCustomerOutput)
        .orElseThrow(() -> new BaseException(ErrorCode.CUSTOMER_NOT_EXISTED));
  }
}
