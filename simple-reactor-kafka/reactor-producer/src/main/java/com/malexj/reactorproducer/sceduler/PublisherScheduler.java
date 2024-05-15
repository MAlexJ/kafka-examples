package com.malexj.reactorproducer.sceduler;

import com.malexj.reactorproducer.kafka.producer.KafkaProducer;
import com.malexj.reactorproducer.model.Message;
import java.time.Duration;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.sender.SenderResult;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PublisherScheduler {

  private final KafkaProducer producer;

  /**
   * Scheduled and Spring webflux link: <a
   * href="https://stackoverflow.com/questions/54093132/scheduled-and-spring-webflux">@Scheduled and
   * Spring webflux</a>
   */
  @Bean
  public Disposable processingSubscriptions() {
    return Flux.interval(Duration.ofSeconds(15))
        // run every minute
        .publishOn(Schedulers.boundedElastic())
        .onBackpressureDrop()
        // if the task below takes a long time, greater than the next tick, then just drop this tick
        .concatMap(r -> processing(), 0)
        .subscribe();
  }

  private Mono<SenderResult<Void>> processing() {
    return Mono.defer(
        () -> {
          var message = new Message(new Random().nextInt(), UUID.randomUUID().toString());
          return producer.send(message);
        });
  }
}
