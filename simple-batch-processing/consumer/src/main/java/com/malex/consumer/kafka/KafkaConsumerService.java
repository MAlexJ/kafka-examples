package com.malex.consumer.kafka;

import com.malex.consumer.event.MessageEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

  @KafkaListener(topics = "${kafka.topic.in}")
  public void receive(
      @Payload List<MessageEvent> messages,
      @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
      @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

    for (int i = 0; i < messages.size(); i++) {
      log.info(
          "Received message='{}' with partition-offset='{}'",
          messages.get(i),
          partitions.get(i) + "-" + offsets.get(i));
    }
    log.info("All batch messages received");
  }
}
