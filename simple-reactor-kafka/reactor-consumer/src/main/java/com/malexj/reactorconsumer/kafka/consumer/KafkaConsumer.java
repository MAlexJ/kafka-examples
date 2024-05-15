package com.malexj.reactorconsumer.kafka.consumer;

import com.malexj.reactorconsumer.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

  private final ReactiveKafkaConsumerTemplate<String, Message> reactiveKafkaConsumer;

  @EventListener(ApplicationStartedEvent.class)
  public Flux<Message> consumerEventListener() {
    return reactiveKafkaConsumer
        .receiveAutoAck()
        .doOnNext(
            consumerRecord ->
                log.info(
                    "received key={}, value={} from topic={}, offset={}",
                    consumerRecord.key(),
                    consumerRecord.value(),
                    consumerRecord.topic(),
                    consumerRecord.offset()))
        .doOnNext(
            result ->
                log.info("successfully result key -{} value - {}", result.key(), result.value()))
        .map(ConsumerRecord::value)
        .doOnError(
            throwable ->
                log.error("something bad happened while consuming : {}", throwable.getMessage()));
  }
}
