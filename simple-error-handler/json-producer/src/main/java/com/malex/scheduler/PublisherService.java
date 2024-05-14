package com.malex.scheduler;

import com.malex.model.Message;
import com.malex.producer.KafkaProducer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublisherService {

  private final KafkaProducer producer;

  private final AtomicInteger counter = new AtomicInteger(0);

  @Scheduled(cron = "*/15 * * * * *")
  public void send() {
    var id = UUID.randomUUID().toString();
    var message = new Message(counter.getAndIncrement(), "message with id: " + id);
    log.info("Send message - {}", message);
    producer.send(id, message);
  }
}
