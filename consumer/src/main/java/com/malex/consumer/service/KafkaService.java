package com.malex.consumer.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaService {

  @KafkaListener(topics = "${cloud.kafka.topic}")
  public void processMessage(
      String message, //
      @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions, //
      @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics, //
      @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
    errorHandler(
        message,
        () ->
            log.info(
                "topic:{}, partition:{}, offset: {}, message: {}",
                topics,
                partitions,
                offsets,
                message));
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
