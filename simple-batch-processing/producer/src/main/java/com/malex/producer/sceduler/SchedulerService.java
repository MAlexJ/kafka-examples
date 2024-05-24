package com.malex.producer.sceduler;

import com.malex.producer.event.MessageEvent;
import com.malex.producer.kafka.KafkaProducerService;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulerService {

  private final KafkaProducerService service;

  private final AtomicInteger counter = new AtomicInteger(0);

  @Scheduled(cron = "*/15 * * * * *")
  public void pushMessageToTopic() {
    var id = UUID.randomUUID().toString();
    service.send(id, new MessageEvent(counter.getAndIncrement(), "Message: " + id));
  }
}
