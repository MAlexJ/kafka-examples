package com.malex.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@Component
@Configuration
public class AppPropertiesConfig {
  @Value("${kafka.bootstrap.servers.url}")
  private String kafkaBootstrapServersUrl;

  @Value("${kafka.groupId}")
  private String kafkaConsumerGroupId;

  @Value("${kafka.topic}")
  private String kafkaTopicAccessory;

  @Value("${kafka.security.sasl.protocol}")
  private String securitySaslProtocol;

  @Value("${kafka.security.sasl.mechanism}")
  private String securitySaslMechanism;

  @Value("${kafka.security.sasl.jaas.config}")
  private String securitySaslJaasConfig;
}
