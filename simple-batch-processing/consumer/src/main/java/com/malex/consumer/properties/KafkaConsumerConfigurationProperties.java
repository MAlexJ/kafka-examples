package com.malex.consumer.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kafka.consumer")
public class KafkaConsumerConfigurationProperties {
    private String autoOffsetReset;
    private String groupId;
    private String keyDeserializer;
    private String valueDeserializer;
    private String propertySpringJsonTrustedPackages;
}
