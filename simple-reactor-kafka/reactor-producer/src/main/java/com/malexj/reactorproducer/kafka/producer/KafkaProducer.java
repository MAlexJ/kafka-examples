package com.malexj.reactorproducer.kafka.producer;

import com.malexj.reactorproducer.model.Message;
import com.malexj.reactorproducer.properties.KafkaConfigProperties;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

  private final KafkaConfigProperties properties;

  private final ReactiveKafkaProducerTemplate<String, Message> reactiveKafkaProducer;

  public Mono<SenderResult<Void>> send(Message message) {
    return reactiveKafkaProducer
        .send(properties.getTopicOut(), UUID.randomUUID().toString(), message)
        .doOnSuccess(
            senderResult -> {
              Exception exception = senderResult.exception();
              if (exception != null) {
                log.warn("Exception occurred while sending message", exception);
              }
              var recordMetadata = senderResult.recordMetadata();
              log.info(
                  "sent {}, topic {}, partition {},  offset : {}",
                  message,
                  recordMetadata.topic(),
                  recordMetadata.partition(),
                  recordMetadata.offset());
            });
  }
}
