package com.malex.producer.configuration;

import com.malex.producer.event.MessageEvent;
import com.malex.producer.property.KafkaConfigurationProperties;
import com.malex.producer.property.KafkaProducerConfigurationProperties;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * Kafka producer configuration <br>
 * link: https://www.baeldung.com/spring-kafka#1-producer-configuration
 */
@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {

  private final KafkaConfigurationProperties properties;
  private final KafkaProducerConfigurationProperties producerProperties;

  @Bean
  public KafkaTemplate<String, MessageEvent> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean
  public ProducerFactory<String, MessageEvent> producerFactory() {
    var props = new HashMap<String, Object>() {};
    // base configuration
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServer());
    props.put(
        CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, properties.getPropertySecurityProtocol());
    props.put(SaslConfigs.SASL_MECHANISM, properties.getPropertySaslMechanism());
    props.put(SaslConfigs.SASL_JAAS_CONFIG, properties.getPropertySaslJaasConfig());

    // producer configuration
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, producerProperties.getKeySerializer());
    props.put(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, producerProperties.getValueSerializer());
    props.put(
        ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,
        producerProperties.getPropertyEnableIdempotence());
    props.put(
        JsonSerializer.ADD_TYPE_INFO_HEADERS,
        producerProperties.getPropertySpringJsonAddTypeHeaders());
    return new DefaultKafkaProducerFactory<>(props);
  }
}
