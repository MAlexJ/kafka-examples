package com.malex.concumet_to_producer.config;

import java.util.HashMap;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

@EmbeddedKafka
public abstract class AbstractEmbeddedKafkaProducer extends AbstractEmbeddedKafkaConsumer {

  protected static final long DURATION = 5000;

  protected Producer<String, String> defaultKafkaProducer;

  public AbstractEmbeddedKafkaProducer(String topicOutput) {
    super(topicOutput);
  }

  /** Set up default Kafka producer factory */
  @BeforeEach
  void setUp() {
    var kafkaProducerFactory = defaultKafkaProducerFactory();
    defaultKafkaProducer = kafkaProducerFactory.createProducer();
  }

  private DefaultKafkaProducerFactory<String, String> defaultKafkaProducerFactory() {
    var configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
    return new DefaultKafkaProducerFactory<>(
        configs, new StringSerializer(), new StringSerializer());
  }

  @AfterEach
  void shutdown() {
    defaultKafkaProducer.close();
  }
}
