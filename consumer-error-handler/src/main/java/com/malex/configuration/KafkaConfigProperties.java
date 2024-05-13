package com.malex.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@Component
@Configuration
public class KafkaConfigProperties {

  @Value("${kafka.bootstrap.servers.url}")
  private String bootstrapServersUrl;

  @Value("${kafka.groupId}")
  private String consumerGroupId;

  @Value("${kafka.topic}")
  private String topic;

  @Value("${kafka.security.sasl.protocol}")
  private String securitySaslProtocol;

  @Value("${kafka.security.sasl.mechanism}")
  private String securitySaslMechanism;

  @Value("${kafka.security.sasl.jaas.config}")
  private String securitySaslJaasConfig;
}
