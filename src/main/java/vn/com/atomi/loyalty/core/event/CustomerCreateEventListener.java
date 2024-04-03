package vn.com.atomi.loyalty.core.event;

import java.util.LinkedHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import vn.com.atomi.loyalty.base.event.BaseRetriesMessageListener;
import vn.com.atomi.loyalty.base.event.MessageData;
import vn.com.atomi.loyalty.core.service.CustomerService;

@SuppressWarnings({"rawtypes"})
@RequiredArgsConstructor
@Component
public class CustomerCreateEventListener extends BaseRetriesMessageListener<LinkedHashMap> {
  private final CustomerService memberService;

  @KafkaListener(
      topics = "${custom.properties.kafka.topic.customer-create-event.name}",
      groupId = "${custom.properties.messaging.kafka.groupId}",
      concurrency = "${custom.properties.kafka.topic.customer-create-event.concurrent.thread}",
      containerFactory = "kafkaListenerContainerFactory")
  public void createCustomerListener(
      String data,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
      @Header(KafkaHeaders.RECEIVED_PARTITION) String partition,
      @Header(KafkaHeaders.OFFSET) String offset,
      Acknowledgment acknowledgment) {
    super.messageListener(data, topic, partition, offset, acknowledgment, 300, 5);
  }

  @KafkaListener(
      topics = "${custom.properties.kafka.topic.customer-create-event-retries.name}",
      groupId = "${custom.properties.messaging.kafka.groupid}",
      concurrency = "1",
      containerFactory = "kafkaListenerContainerFactory")
  public void createCustomerRetriesListener(
      String data,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
      @Header(KafkaHeaders.RECEIVED_PARTITION) String partition,
      @Header(KafkaHeaders.OFFSET) String offset,
      Acknowledgment acknowledgment) {
    super.messageRetriesListener(data, topic, partition, offset, acknowledgment);
  }

  @Override
  protected void handleMessageEvent(
      String topic,
      String partition,
      String offset,
      MessageData<LinkedHashMap> input,
      String messageId) {
    memberService.creates(messageId, input.getContents());
  }
}
