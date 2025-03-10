package com.malex;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import com.malex.consumer.KafkaConsumer;
import com.malex.event.MessageEvent;
import com.malex.producer.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(
    partitions = 1,
    brokerProperties = {"listeners=PLAINTEXT://localhost:9992", "port=9992"})
class EmbeddedKafkaUnitTest {

  private static final long DURATION = 5000;

  @MockitoSpyBean private KafkaConsumer consumer;

  @Autowired private KafkaProducer producer;

  @Captor private ArgumentCaptor<String> myMessageCaptor;

  @Test
  void embeddedKafkaBrokerSendTest() {
    // given
    String data = "Sending with our own simple KafkaProducer";

    // when
    producer.send(new MessageEvent(1, data));

    // then
    verify(consumer, timeout(DURATION)).processMessage(myMessageCaptor.capture());
    assertThat(myMessageCaptor.getValue()).contains(data);
  }
}
