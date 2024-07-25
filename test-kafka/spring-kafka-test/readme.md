### Kafka test

link: https://www.baeldung.com/spring-boot-kafka-testing
link: https://for-each.dev/lessons/b/-spring-boot-kafka-testing

#### Unit test: Kafka producer and consumer tests

link: https://stackoverflow.com/questions/52783066/how-to-write-unit-test-for-kafkalistener

> In unit test, how do I ensure @KafkaListener is invoked when a new message is arrived in Kafka.

Instead of using Awaitility or CountDownLatch approach, a more easy way is to make the actual @KafkaListener bean
as the mockito spy using @SpyBean. Spy basically allows you to record all interactions made on an actual bean instance
such that you can verify its interactions later.

Together with the timeout verify feature of the mockito , you can ensure that the verification will be done over
and over until certain timeout after the producer send out the message.

Something like :

```java

@SpringBootTest(properties = {"spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"})
@EmbeddedKafka(topics = {"fooTopic"})
public class MyMessageConsumersTest {

    @SpyBean
    private MyKafkaListener myKafkaListener;

    @Captor
    private ArgumentCaptor<MyMessage> myMessageCaptor;

    @Test
    public void test() {

        //create KafkaTemplate to send some message to the topic...

        verify(myKafkaListener, timeout(5000)).myMessageListener(myMessageCaptor.capture());

        //assert the KafkaListener is configured correctly such that it is invoked with the expected parameter
        assertThat(myMessageCaptor.getValue()).isEqualTo(xxxxx);

    }
```
