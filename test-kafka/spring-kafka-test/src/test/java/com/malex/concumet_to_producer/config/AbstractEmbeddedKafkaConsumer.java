package com.malex.concumet_to_producer.config;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;

@EmbeddedKafka
public abstract class AbstractEmbeddedKafkaConsumer extends AbstractEmbeddedKafkaBroker {

  private final String[] topics;

  private BlockingQueue<ConsumerRecord<String, String>> records;

  private KafkaMessageListenerContainer<String, String> defaultKafkaConsumer;

  protected AbstractEmbeddedKafkaConsumer(String... consumerTopics) {
    this.topics = Objects.requireNonNull(consumerTopics, "Consumer topics cannot be null!");
  }

  protected BlockingQueue<ConsumerRecord<String, String>> getConsumerRecord() {
    return records;
  }

  /** Set up default Kafka consumer factory */
  @BeforeEach
  void setUpConsumerFactory() {
    var consumerFactory = new DefaultKafkaConsumerFactory<String, String>(consumerProperties());
    var containerProperties = new ContainerProperties(topics);
    defaultKafkaConsumer =
        new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    records = new LinkedBlockingQueue<>();
    defaultKafkaConsumer.setupMessageListener((MessageListener<String, String>) records::add);
    defaultKafkaConsumer.start();
    ContainerTestUtils.waitForAssignment(
        defaultKafkaConsumer, embeddedKafkaBroker.getPartitionsPerTopic());
  }

  private Map<String, Object> consumerProperties() {
    return Map.of(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString(),
        ConsumerConfig.GROUP_ID_CONFIG, "consumer",
        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true",
        ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1",
        ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "10000",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class
        //  wtf:   ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
        );
  }

  @AfterEach
  void tearDown() {
    defaultKafkaConsumer.stop();
  }
}
