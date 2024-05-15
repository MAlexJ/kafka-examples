package com.malexj.reactorconsumer.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "consumer.kafka")
public class KafkaConsumerConfigurationProperties {
  private String bootstrapServerUrl;
  private String topic;
  private String groupId;
  private String securitySaslProtocol;
  private String securitySaslMechanism;
  private String securitySaslJaasConfig;
}
