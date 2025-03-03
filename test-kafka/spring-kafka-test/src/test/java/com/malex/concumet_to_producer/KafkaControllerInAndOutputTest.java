package com.malex.concumet_to_producer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import com.malex.concumet_to_producer.config.AbstractEmbeddedKafkaProducer;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

/** info https://www.geekyhacker.com/test-spring-kafka-consumer-and-producer-with-embeddedkafka/ */
@SpringBootTest
@DirtiesContext
class KafkaControllerInAndOutputTest extends AbstractEmbeddedKafkaProducer {

  @MockitoSpyBean private KafkaConsumerInput consumerInput;

  @Captor ArgumentCaptor<String> messageArgumentCaptor;

  @Value("${cloud.kafka.topic_input}")
  private String topicInput;

  public KafkaControllerInAndOutputTest(@Value("${cloud.kafka.topic_output}") String topicOutput) {
    super(topicOutput);
  }

  @Test
  void messagePassConditionTest() throws InterruptedException {
    // 1. Sent message to 'input' topic
    var key = UUID.randomUUID().toString();
    var value = "test message";
    defaultKafkaProducer.send(new ProducerRecord<>(topicInput, key, value));

    // 2. catch message event in kafka consumer
    verify(consumerInput, timeout(DURATION).times(1))
        .processMessage(messageArgumentCaptor.capture());
    assertThat(messageArgumentCaptor.getValue()).contains(value);

    // 3. verify result in 'output' topic
    ConsumerRecord<String, String> message =
        getConsumerRecord().poll(DURATION, TimeUnit.MILLISECONDS);
    assertNotNull(message);
    assertThat(message.value()).contains(value);
  }

  @Test
  void messageDidntPassConditionTest() throws InterruptedException {
    // 1. sent message to input topic
    var key = UUID.randomUUID().toString();
    var value = "xxx message";
    defaultKafkaProducer.send(new ProducerRecord<>(topicInput, key, value));

    // 2. catch message in kafka consumer
    verify(consumerInput, timeout(DURATION).times(1))
        .processMessage(messageArgumentCaptor.capture());
    assertThat(messageArgumentCaptor.getValue()).contains(value);

    // 3. verify result topic
    ConsumerRecord<String, String> message =
        getConsumerRecord().poll(DURATION, TimeUnit.MILLISECONDS);
    assertNull(message);
  }
}
