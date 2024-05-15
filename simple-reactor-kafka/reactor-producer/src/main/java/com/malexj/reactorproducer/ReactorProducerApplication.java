package com.malexj.reactorproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Reactor Kafka Producer/Consumer with Spring Boot <a
 * href="https://medium.com/@erkndmrl/reactor-kafka-producer-consumer-with-springboot-fe14d07d5616">Reactor
 * Kafka Producer/Consumer</a>
 */
@SpringBootApplication
public class ReactorProducerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ReactorProducerApplication.class, args);
  }
}
