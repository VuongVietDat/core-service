package vn.com.atomi.loyalty.core.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.base.exception.CommonErrorCode;
import vn.com.atomi.loyalty.core.dto.output.RankOutput;
import vn.com.atomi.loyalty.core.entity.Customer;
import vn.com.atomi.loyalty.core.entity.CustomerBalance;
import vn.com.atomi.loyalty.core.entity.CustomerRank;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.feign.LoyaltyConfigClient;
import vn.com.atomi.loyalty.core.repository.CustomerBalanceRepository;
import vn.com.atomi.loyalty.core.repository.CustomerRankRepository;
import vn.com.atomi.loyalty.core.repository.CustomerRepository;
import vn.com.atomi.loyalty.core.utils.Utils;

@SuppressWarnings({"rawtypes"})
@RequiredArgsConstructor
@Component
public class CustomerCreateEventListener extends MessageListener<LinkedHashMap> {
  private final LoyaltyConfigClient configClient;
  private final CustomerRepository customerRepository;
  private final CustomerBalanceRepository customerBalanceRepository;
  private final CustomerRankRepository customerRankRepository;
  private final ObjectMapper mapper;

  @KafkaListener(
      topics = "${custom.properties.kafka.topic.customer-create-event.name}",
      groupId = "${custom.properties.messaging.kafka.groupId}",
      concurrency = "${custom.properties.kafka.topic.customer-create-event.concurrent.thread}",
      containerFactory = "kafkaListenerContainerFactory")
  public void workflowEventListener(
      String data,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
      @Header(KafkaHeaders.RECEIVED_PARTITION) String partition,
      @Header(KafkaHeaders.OFFSET) String offset,
      Acknowledgment acknowledgment) {
    super.messageListener(data, topic, partition, offset, acknowledgment, 300, 10);
  }

  @KafkaListener(
      topics = "${custom.properties.kafka.topic.customer-create-event-retries.name}",
      groupId = "${custom.properties.messaging.kafka.groupid}",
      concurrency = "1",
      containerFactory = "kafkaListenerContainerFactory")
  public void workflowEventRetriesListener(
      String data,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
      @Header(KafkaHeaders.RECEIVED_PARTITION) String partition,
      @Header(KafkaHeaders.OFFSET) String offset,
      Acknowledgment acknowledgment) {
    super.messageRetriesListener(data, topic, partition, offset, acknowledgment);
  }

  @Transactional
  @Override
  public void handleMessageEvent(
      String topic,
      String partition,
      String offset,
      MessageData<LinkedHashMap> input,
      String messageId) {
    var currentDate = LocalDate.now();
    var rankCode = getFirstRank();
    // lay cac next ID
    var customerSequence = customerRepository.getSequence();
    var customerBalanceSequence = customerBalanceRepository.getSequence();
    var customerRankSequence = customerRankRepository.getSequence();

    // init data
    var listCustomer = new ArrayList<Customer>();
    var listCustomerBalances = new ArrayList<CustomerBalance>();
    var listCustomerRanks = new ArrayList<CustomerRank>();
    var contents = input.getContents();
    var n = contents.size();
    for (int i = 0; i < n; i++) {
      listCustomer.add(mapper.convertValue(contents.get(i), Customer.class));

      listCustomerBalances.add(
          CustomerBalance.builder()
              .customerId(customerSequence + i)
              .code(
                  Utils.generateCode(
                      customerBalanceSequence + i, CustomerBalance.class.getSimpleName()))
              .totalAmount(0L)
              .lockAmount(0L)
              .availableAmount(0L)
              .totalPointsUsed(0L)
              .totalAccumulatedPoints(0L)
              .totalPointsExpired(0L)
              .status(Status.ACTIVE)
              .build());

      listCustomerRanks.add(
          CustomerRank.builder()
              .customerId(customerSequence + i)
              .code(
                  Utils.generateCode(customerRankSequence + i, CustomerRank.class.getSimpleName()))
              .rank(rankCode)
              .applyDate(currentDate)
              .totalPoint(0L)
              .status(Status.ACTIVE)
              .build());
    }

    // saves
    customerRepository.saveAll(listCustomer);
    customerBalanceRepository.saveAll(listCustomerBalances);
    customerRankRepository.saveAll(listCustomerRanks);
  }

  private String getFirstRank() {
    var sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    var requestId = sra.getRequest().getHeader(RequestConstant.REQUEST_ID);
    var res = configClient.getAllRanks(requestId);
    if (res.getCode() != 0) throw new BaseException(CommonErrorCode.EXECUTE_THIRTY_SERVICE_ERROR);
    var ranks = res.getData();
    return ranks.stream()
        .min(Comparator.comparing(RankOutput::getId))
        .orElseThrow(() -> new BaseException(CommonErrorCode.ENTITY_NOT_FOUND))
        .getCode();
  }
}
