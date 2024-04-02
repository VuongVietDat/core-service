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
import vn.com.atomi.loyalty.base.utils.JsonUtils;
import vn.com.atomi.loyalty.core.dto.message.AllocationPointMessage;
import vn.com.atomi.loyalty.core.service.AllocationPointService;

/**
 * @author haidv
 * @version 1.0
 */
@SuppressWarnings({"rawtypes"})
@RequiredArgsConstructor
@Component
public class AllocationPointEventListener extends BaseRetriesMessageListener<LinkedHashMap> {

  private final AllocationPointService allocationPointService;

  @KafkaListener(
      topics = "${custom.properties.kafka.topic.allocation-point-event.name}",
      groupId = "${custom.properties.messaging.kafka.groupId}",
      concurrency = "${custom.properties.kafka.topic.allocation-point-event.concurrent.thread}",
      containerFactory = "kafkaListenerContainerFactory")
  public void allocationPointEventListener(
      String data,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
      @Header(KafkaHeaders.RECEIVED_PARTITION) String partition,
      @Header(KafkaHeaders.OFFSET) String offset,
      Acknowledgment acknowledgment) {
    super.messageListener(data, topic, partition, offset, acknowledgment, 300, 15);
  }

  @KafkaListener(
      topics = "${custom.properties.kafka.topic.allocation-point-event-retries.name}",
      groupId = "${custom.properties.messaging.kafka.groupId}",
      concurrency = "1",
      containerFactory = "kafkaListenerContainerFactory")
  public void allocationPointEventRetriesListener(
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
    allocationPointService.handlerAllocationPointEvent(
        JsonUtils.fromJson(input.getContents().get(0), AllocationPointMessage.class));
  }
}
