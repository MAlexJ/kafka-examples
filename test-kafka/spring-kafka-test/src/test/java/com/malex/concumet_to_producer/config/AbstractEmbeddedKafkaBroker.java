package com.malex.concumet_to_producer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

public abstract class AbstractEmbeddedKafkaBroker {

  @Autowired protected EmbeddedKafkaBroker embeddedKafkaBroker;
}
