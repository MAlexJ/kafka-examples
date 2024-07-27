package com.malex.only_consumer_test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import com.malex.consumer.KafkaConsumer;
import com.malex.event.MessageEvent;
import java.util.HashMap;
import java.util.UUID;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Test Spring Kafka consumer and producer with EmbeddedKafka
 *
 * <p>info: https://www.geekyhacker.com/test-spring-kafka-consumer-and-producer-with-embeddedkafka/
 */
@SpringBootTest
@DirtiesContext
@EmbeddedKafka
public class EmbeddedKafkaConsumerUnitTest {

  protected static final long DURATION = 5000;

  @Value("${cloud.kafka.topic}")
  private String topic;

  @SpyBean private KafkaConsumer consumer;

  @Autowired private EmbeddedKafkaBroker embeddedKafkaBroker;

  private Producer<String, String> defaultKafkaProducer;

  @Captor ArgumentCaptor<String> messageArgumentCaptor;

  /** Set up default Kafka producer factory */
  @BeforeEach
  void setUpProducerFactory() {
    var configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
    var keySerializer = new StringSerializer();
    var valueSerializer = new StringSerializer();
    defaultKafkaProducer =
        new DefaultKafkaProducerFactory<>(configs, keySerializer, valueSerializer).createProducer();
  }

  @AfterEach
  void shutdown() {
    defaultKafkaProducer.close();
  }

  @Test
  public void testLogKafkaMessages() {
    // given
    var message = new MessageEvent(1, "Hello!");

    // when
    defaultKafkaProducer.send(
        new ProducerRecord<>(topic, 0, UUID.randomUUID().toString(), message.toString()));
    defaultKafkaProducer.flush();

    // than
    verify(consumer, timeout(DURATION).times(1)).processMessage(messageArgumentCaptor.capture());

    // and
    assertThat(messageArgumentCaptor.getValue()).contains("Hello!");
  }
}
