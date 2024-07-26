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

  @Value("${cloud.kafka.topic}")
  private String topic;

  @SpyBean private KafkaConsumer consumer;

  @Autowired private EmbeddedKafkaBroker embeddedKafkaBroker;

  private Producer<String, String> producer;

  @Captor ArgumentCaptor<String> messageArgumentCaptor;

  @Captor ArgumentCaptor<String> topicArgumentCaptor;

  @Captor ArgumentCaptor<Integer> partitionArgumentCaptor;

  @Captor ArgumentCaptor<Long> offsetArgumentCaptor;

  @BeforeEach
  void setUp() {
    var configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
    var keySerializer = new StringSerializer();
    var valueSerializer = new StringSerializer();
    producer =
        new DefaultKafkaProducerFactory<>(configs, keySerializer, valueSerializer).createProducer();
  }

  @AfterEach
  void shutdown() {
    producer.close();
  }

  @Test
  public void testLogKafkaMessages() {
    // given
    var message = new MessageEvent(1, "Hello!");

    // when
    producer.send(new ProducerRecord<>(topic, 0, UUID.randomUUID().toString(), message.toString()));
    producer.flush();

    // than
    verify(consumer, timeout(500).times(1)).processMessage(messageArgumentCaptor.capture());

    // and
    assertThat(messageArgumentCaptor.getValue()).contains("Hello!");

    //        User user = userArgumentCaptor.getValue();
    //        assertNotNull(user);
    //        assertEquals("11111", user.getUuid());
    //        assertEquals("John", user.getFirstName());
    //        assertEquals("Wick", user.getLastName());
    //        assertEquals(TOPIC_NAME, topicArgumentCaptor.getValue());
    //        assertEquals(0, partitionArgumentCaptor.getValue());
    //        assertEquals(0, offsetArgumentCaptor.getValue());
  }
}
