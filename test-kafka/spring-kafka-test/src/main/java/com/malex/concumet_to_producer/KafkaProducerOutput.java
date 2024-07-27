package com.malex.concumet_to_producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerOutput {

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Value("${cloud.kafka.topic_output}")
  private String topic;

  public void send(String message) {
    log.info(">>> Send message - {}", message);
    kafkaTemplate
        .send(topic, message)
        .thenAccept(
            result -> {
              var metadata = result.getRecordMetadata();
              var offset = metadata.offset();
              var partition = metadata.partition();
              log.info(
                  ">>> Producer - Message was sent, topic - {}, partition - {}, offset - {}",
                  metadata.topic(),
                  partition,
                  offset);
            });
  }
}
