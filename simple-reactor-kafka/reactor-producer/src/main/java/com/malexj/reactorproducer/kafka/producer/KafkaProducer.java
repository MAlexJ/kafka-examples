package com.malexj.reactorproducer.kafka.producer;

import com.malexj.reactorproducer.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

  @Value("${kafka.topic.out}")
  private String topic;

  private final ReactiveKafkaProducerTemplate<String, Message> reactiveKafkaProducer;

  public Mono<SenderResult<Void>> send(Message message) {
    log.info("send to topic={}, {}={},", topic, Message.class.getSimpleName(), message);
    return reactiveKafkaProducer
        .send(topic, message)
        .doOnSuccess(
            senderResult ->
                log.info("sent {} offset : {}", message, senderResult.recordMetadata().offset()));
  }
}
