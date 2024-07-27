package com.malex.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

  @KafkaListener(topics = "${cloud.kafka.topic}")
  public void processMessage(String message) {
    errorHandler(message, () -> log.info("Consumer - topic:{}", message));
  }

  private void errorHandler(String message, Runnable r) {
    try {
      r.run();
    } catch (Exception ex) {
      String errorMessage =
          String.format("Can't process message - [%s], error - %s", message, ex.getMessage());
      log.error(errorMessage);
    }
  }
}
