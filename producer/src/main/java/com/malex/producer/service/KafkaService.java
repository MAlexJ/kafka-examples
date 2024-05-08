package com.malex.producer.service;

import com.malex.producer.model.Message;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Value("${cloud.kafka.topic}")
  private String topic;

  private final AtomicInteger counter = new AtomicInteger();

  @Transactional
  @Scheduled(cron = "*/30 * * * * *")
  public void send() {
    var message = new Message(counter.getAndIncrement(), UUID.randomUUID().toString());
    log.info("Send message - {}", message);
    kafkaTemplate.send(topic, message.toString());
  }
}
