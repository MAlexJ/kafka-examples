package com.malex.configuration;

import com.malex.handler.KafkaCommonErrorHandler;
import com.malex.model.Message;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
@ConditionalOnProperty(name = "kafka.default.enabled", matchIfMissing = true)
@RequiredArgsConstructor
public class KafkaConsumerConfig {

  private final KafkaConfigProperties properties;

  /**
   * Kafka consumer factory setup - wrapper for concurrency
   *
   * @return wrapped factory
   */
  @Bean("kafkaListenerContainerFactory")
  public ConcurrentKafkaListenerContainerFactory<String, Message> kafkaListenerContainerFactory() {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, Message>();
    factory.setConsumerFactory(consumerFactory());
    factory.setCommonErrorHandler(new KafkaCommonErrorHandler());
    return factory;
  }

  /**
   * Kafka consumer factory setup - standard factory.
   *
   * <p>configuration SASL_SSL connection: <a
   * href="https://stackoverflow.com/questions/60825373/spring-kafka-application-properties-configuration-for-jaas-sasl-not-working">configuration
   * for JAAS/SASL</a>
   */
  @Bean
  public ConsumerFactory<String, Message> consumerFactory() {
    var props = new HashMap<String, Object>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServersUrl());
    props.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getConsumerGroupId());
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    /*
     * Configuration SASL_SSL connection: <a
     * href="https://stackoverflow.com/questions/60825373/spring-kafka-application-properties-configuration-for-jaas-sasl-not-working">JAAS/SASL</a>
     */
    props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, properties.getSecuritySaslProtocol());
    props.put(SaslConfigs.SASL_MECHANISM, properties.getSecuritySaslMechanism());
    props.put(SaslConfigs.SASL_JAAS_CONFIG, properties.getSecuritySaslJaasConfig());

    // key.deserializer
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    // value.deserializer
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    // spring.deserializer.key.delegate.class
    props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, JsonDeserializer.class);

    return new DefaultKafkaConsumerFactory<>(
        props, new StringDeserializer(), createMessageErrorHandlingDeserializer());
  }

  private ErrorHandlingDeserializer<Message> createMessageErrorHandlingDeserializer() {
    return new ErrorHandlingDeserializer<>(new JsonDeserializer<>(Message.class));
  }
}
