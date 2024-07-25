package com.malex.producer;

import com.malex.event.MessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Value("${cloud.kafka.topic}")
  private String topic;

  public void send(MessageEvent event) {
    log.info("Send message event - {}", event);
    kafkaTemplate
        .send(topic, event.toString())
        .thenAccept(
            result -> {
              var metadata = result.getRecordMetadata();
              var offset = metadata.offset();
              var partition = metadata.partition();
              log.info(
                  "Producer - Message was sent, topic - {}, partition - {}, offset - {}",
                  metadata.topic(),
                  partition,
                  offset);
            });
  }
}
