package com.malex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class JsonProducerApplication {

  public static void main(String[] args) {
    SpringApplication.run(JsonProducerApplication.class, args);
  }
}
