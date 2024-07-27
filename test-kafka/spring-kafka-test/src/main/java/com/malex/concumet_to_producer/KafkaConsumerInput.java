package com.malex.concumet_to_producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerInput {

  private final KafkaProducerOutput producer;

  @KafkaListener(topics = "${cloud.kafka.topic_input}")
  public void processMessage(String message) {
    log.info(">>> Consumer - topic:{}", message);
    implementation(message);
  }

  private void implementation(String message) {
    if (message.contains("test")) {
      producer.send(message);
    }
  }
}
