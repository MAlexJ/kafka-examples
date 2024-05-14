package com.malex.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;

/**
 * Link: <a
 * href="https://www.baeldung.com/spring-kafka-deserialization-errors#1-implementing-commonerrorhandler">Common
 * Error Handler</a>
 *
 * <ul>
 *   <li>handleOtherException() – called when an exception is thrown, but not for particular record
 *   <li>handleOne() – called to handle a single failed record
 * </ul>
 */
@Slf4j
public class KafkaCommonErrorHandler implements CommonErrorHandler {

  @Override
  public boolean handleOne(
      Exception exception,
      ConsumerRecord<?, ?> record,
      Consumer<?, ?> consumer,
      MessageListenerContainer container) {
    return handle(exception, consumer);
  }

  @Override
  public void handleOtherException(
      Exception exception,
      Consumer<?, ?> consumer,
      MessageListenerContainer container,
      boolean batchListener) {
    handle(exception, consumer);
  }

  private boolean handle(Exception exception, Consumer<?, ?> consumer) {
    log.error("Exception thrown", exception);
    if (exception instanceof RecordDeserializationException ex) {
      consumer.seek(ex.topicPartition(), ex.offset() + 1L);
      consumer.commitSync();
    } else {
      log.error("Exception not handled", exception);
    }
    return true;
  }
}
