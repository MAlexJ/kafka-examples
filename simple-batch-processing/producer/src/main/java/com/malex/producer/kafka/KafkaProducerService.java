package com.malex.producer.kafka;

import com.malex.producer.event.MessageEvent;
import com.malex.producer.property.KafkaTopicConfigurationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

  private final KafkaTemplate<String, MessageEvent> kafkaTemplate;

  private final KafkaTopicConfigurationProperties properties;

  public void send(String id, MessageEvent message) {
    kafkaTemplate
        .send(properties.getOut(), id, message)
        .thenAccept(
            result -> {
              var metadata = result.getRecordMetadata();
              var offset = metadata.offset();
              var partition = metadata.partition();
              log.info(
                  "Message - {}  was sent to topic - {}, partition - {}, offset - {}",
                  message,
                  metadata.topic(),
                  partition,
                  offset);
            });
  }
}
