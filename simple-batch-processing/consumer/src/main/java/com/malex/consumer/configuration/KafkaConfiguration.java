package com.malex.consumer.configuration;

import com.malex.consumer.event.MessageEvent;
import com.malex.consumer.properties.KafkaConfigurationProperties;
import com.malex.consumer.properties.KafkaConsumerConfigurationProperties;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {

  private final KafkaConfigurationProperties properties;
  private final KafkaConsumerConfigurationProperties consumerProperties;

  private final String messageEvenDeserializationClass = MessageEvent.class.getName();

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, MessageEvent> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, MessageEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    factory.setBatchListener(true);
//    factory.setBatchErrorHandler(new BatchLoggingErrorHandler());
    return factory;
  }

  @Bean
  public ConsumerFactory<String, MessageEvent> consumerFactory() {
    Map<String, Object> props = new HashMap<>();
    // base configuration
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServer());
    props.put(
        CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, properties.getPropertySecurityProtocol());
    props.put(SaslConfigs.SASL_MECHANISM, properties.getPropertySaslMechanism());
    props.put(SaslConfigs.SASL_JAAS_CONFIG, properties.getPropertySaslJaasConfig());

    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "4");

    // consumer configuration
    props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerProperties.getGroupId());
    props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
    props.put(
        JsonDeserializer.TRUSTED_PACKAGES,
        consumerProperties.getPropertySpringJsonTrustedPackages());
    props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, messageEvenDeserializationClass);
    props.put(
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, consumerProperties.getKeyDeserializer());
    props.put(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, consumerProperties.getValueDeserializer());
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerProperties.getAutoOffsetReset());

    return new DefaultKafkaConsumerFactory<>(props);
  }
}
