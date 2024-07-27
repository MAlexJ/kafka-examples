package com.malex.only_producer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.malex.event.MessageEvent;
import com.malex.producer.KafkaProducer;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Test Spring Kafka consumer and producer with EmbeddedKafka
 *
 * <p>info: https://www.geekyhacker.com/test-spring-kafka-consumer-and-producer-with-embeddedkafka/
 */
@SpringBootTest
@DirtiesContext
@EmbeddedKafka
public class EmbeddedKafkaProducerUnitTest {

  private BlockingQueue<ConsumerRecord<String, String>> records;

  private KafkaMessageListenerContainer<String, String> defaultKafkaConsumer;

  @Autowired private KafkaProducer producer;

  @Autowired private EmbeddedKafkaBroker embeddedKafkaBroker;

  @Value("${cloud.kafka.topic}")
  private String topic;

  /** Set up default Kafka consumer factory */
  @BeforeEach
  void setUpConsumerFactory() {
    var consumerFactory = new DefaultKafkaConsumerFactory<String, String>(getConsumerProperties());
    var containerProperties = new ContainerProperties(topic);
    defaultKafkaConsumer = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    records = new LinkedBlockingQueue<>();
    defaultKafkaConsumer.setupMessageListener((MessageListener<String, String>) records::add);
    defaultKafkaConsumer.start();
    ContainerTestUtils.waitForAssignment(defaultKafkaConsumer, embeddedKafkaBroker.getPartitionsPerTopic());
  }

  private Map<String, Object> getConsumerProperties() {
    return Map.of(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString(),
        ConsumerConfig.GROUP_ID_CONFIG, "consumer",
        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true",
        ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "10",
        ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "10000",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
  }

  @AfterEach
  void tearDown() {
    defaultKafkaConsumer.stop();
  }

  /**
   * Test Spring Kafka consumer and producer with EmbeddedKafka
   *
   * <p>info:
   * https://www.geekyhacker.com/test-spring-kafka-consumer-and-producer-with-embeddedkafka/
   */
  @Test
  public void embeddedKafkaBrokerSendTest() throws Exception {
    // given
    String data = "Sending with our own simple KafkaProducer";
    var event = new MessageEvent(1, data);

    // when
    producer.send(event);

    // then
    ConsumerRecord<String, String> message = records.poll(500, TimeUnit.MILLISECONDS);
    assertNotNull(message);
    var actualEvent = message.value();

    // and
    assertThat(actualEvent).contains(data, "1");
  }
}
