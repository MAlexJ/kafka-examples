package com.malex.consumer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

  @Getter
  protected AtomicInteger counter = new AtomicInteger(0);

  @KafkaListener(topics = "${cloud.kafka.topic}")
  public void processMessage(
      String message) {
    errorHandler(
        message,
        () ->
            log.info(
                "Consumer - topic:{}",
//                topics,
//                partitions,
//                offsets,
                message));
  }

  private void errorHandler(String message, Runnable r) {
    try {
      counter.incrementAndGet();
      r.run();
    } catch (Exception ex) {
      String errorMessage =
          String.format("Can't process message - [%s], error - %s", message, ex.getMessage());
      log.error(errorMessage);
    }
  }
}
