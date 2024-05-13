# Spring boot kafka examples

## Description

* Event streaming platform - Kafka
* Web framework - Spring Boot 3
* Java version 21

## Cloud kafka

free kafka online - https://www.cloudkarafka.com/ </br>
Free plan: $0 </br>
For testing and development we provide an Apache Kafka server
that's shared between multiple users. </br>
Note that other users' actions may affect your experience on this server.

* Shared cluster
* Topic management
* Max 5 topics with 10 MB data/topic
* Max 28 days retention
* Certificate based authentication
* Consume and produce from Kafka in a UI
* Email and chat support

### How to set up kafka:

link for info - https://customer.cloudkarafka.com/instance </br>
documentation: https://www.cloudkarafka.com/docs/spring.html </br>
Springboot sample: https://github.com/CloudKarafka/springboot-kafka-example </br>

* Hostname with port - url for CLOUD_KAFKA_BROKER_URL
* Default user - username for CLOUD_KAFKA_USERNAME
* oPassword - Password for CLOUD_KAFKA_PASSWORD

### Add ENV properties to project/IDE or .env file

```
CLOUD_KAFKA_USERNAME={Default user}
CLOUD_KAFKA_PASSWORD={Password}
CLOUD_KAFKA_BROKER_URL=test-speedcar-01.srvs.cloudkafka.com:9094
```

### Github action

issue:  ./gradlew: Permission denied
link: https://stackoverflow.com/questions/17668265/gradlew-permission-denied

You need to update the execution permission for gradlew

1. add action workflow

2. Locally pull changes

3. run Git command:

```
git update-index --chmod=+x gradlew
git add .
git commit -m "Changing permission of gradlew"
git push
```