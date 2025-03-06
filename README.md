# Spring boot kafka examples

## Description

* Event streaming platform - Kafka
* Web framework - Spring Boot 3
* Java version 21

## Docker

docker-compose.yml

### How to set up kafka:

link for info - https://customer.cloudkarafka.com/instance </br>
documentation: https://www.cloudkarafka.com/docs/spring.html </br>
Springboot sample: https://github.com/CloudKarafka/springboot-kafka-example </br>

* Hostname with port - url for KAFKA_BROKER_URL
* Default user - username for KAFKA_USERNAME
* oPassword - Password for KAFKA_PASSWORD

### Add ENV properties to project/IDE or .env file

```
KAFKA_USERNAME={Default user}
KAFKA_PASSWORD={Password}
KAFKA_BROKER_URL=test.com:9094
```

### Gradle

### Gradle Versions Plugin

Displays a report of the project dependencies that are up-to-date, exceed the latest version found, have upgrades, or
failed to be resolved, info: https://github.com/ben-manes/gradle-versions-plugin

command:

```
gradle dependencyUpdates
```

#### Gradle wrapper

The recommended way to execute any Gradle build is with the help of the Gradle Wrapper (referred to as "Wrapper")

```
./gradlew wrapper --gradle-version latest
```

#### Gradle ignore test

To skip any task from the Gradle build, we can use the -x or –exclude-task option. In this case, we’ll use “-x test” to
skip tests from the build.

To see it in action, let’s run the build command with -x option:

```
gradle build -x test
```
