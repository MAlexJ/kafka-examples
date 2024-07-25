package com.malex.controller;

import com.malex.event.MessageEvent;
import com.malex.producer.KafkaProducer;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/topics")
public class KafkaRestController {

  private final KafkaProducer producer;

  @PostMapping
  public ResponseEntity<Void> send(@RequestBody MessageEvent event) {
    Objects.requireNonNull(event, "Event must not be not null!");

    producer.send(event);
    return ResponseEntity.ok().build();
  }
}
