package com.malex.producer.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kafka.producer")
public class KafkaProducerConfigurationProperties {
  private String keySerializer;
  private String valueSerializer;
  private Boolean propertyEnableIdempotence;
  private Boolean propertySpringJsonAddTypeHeaders;
}
