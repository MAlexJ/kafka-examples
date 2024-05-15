package com.malexj.reactorproducer.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "producer.kafka")
public class KafkaConfigProperties {
  private String bootstrapServerUrl;
  private String topicOut;
  private String securitySaslProtocol;
  private String securitySaslMechanism;
  private String securitySaslJaasConfig;
}
