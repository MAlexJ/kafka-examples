package com.malex.producer.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kafka.server")
public class KafkaConfigurationProperties {
  private String bootstrapServer;
  private String propertySecurityProtocol;
  private String propertySaslMechanism;
  private String propertySaslJaasConfig;
}
