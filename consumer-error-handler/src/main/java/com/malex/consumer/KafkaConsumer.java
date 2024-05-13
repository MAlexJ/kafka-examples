package com.malex.consumer;

import com.malex.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

  @KafkaListener(
      topics = "#{'${kafka.topic}'.split(',')}",
      containerFactory = "kafkaListenerContainerFactory")
  public void processPayment(Message message) {
    log.info("Message processed: {}", message);
  }
}
