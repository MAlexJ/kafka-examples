package com.malexj.reactorconsumer.configuration;

import com.malexj.reactorconsumer.model.Message;
import com.malexj.reactorconsumer.properties.KafkaConsumerConfigurationProperties;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.ReceiverOptions;

@Configuration
@RequiredArgsConstructor
public class ReactiveKafkaConsumerConfig {

  private final KafkaConsumerConfigurationProperties properties;

  @Bean
  public ReceiverOptions<String, Message> kafkaReceiver() {

    Map<String, Object> config = new HashMap<>();
    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServerUrl());
    config.put(ConsumerConfig.GROUP_ID_CONFIG, "reactive-kafka");

    /*
     * Configuration SASL_SSL connection: <a
     * href="https://stackoverflow.com/questions/60825373/spring-kafka-application-properties-configuration-for-jaas-sasl-not-working">JAAS/SASL</a>
     */
    config.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, properties.getSecuritySaslProtocol());
    config.put(SaslConfigs.SASL_MECHANISM, properties.getSecuritySaslMechanism());
    config.put(SaslConfigs.SASL_JAAS_CONFIG, properties.getSecuritySaslJaasConfig());

    config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
    config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.reactive.kafka.model.ConsumerSample");
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    ReceiverOptions<String, Message> basicReceiverOptions = ReceiverOptions.create(config);
    return basicReceiverOptions.subscription(Collections.singletonList(properties.getTopic()));
  }

  @Bean
  public ReactiveKafkaConsumerTemplate<String, Message> reactiveKafkaConsumer(
      ReceiverOptions<String, Message> kafkaReceiverOptions) {
    return new ReactiveKafkaConsumerTemplate<>(kafkaReceiverOptions);
  }
}
