FROM gradle:8.7-jdk21-alpine AS build

# COPY has two forms. The latter form is required for paths containing whitespace.
# 1. COPY [OPTIONS] <src> ... <dest>
# 2. <src>: COPY . /home/gradle/src
#    take files and folders from the local build context and add them to the current working directory of the image
# 3. [OPTIONS]: --chown - attempts to change the owner of the file filename to user user.
CMD ../
COPY --chown=gradle:gradle . /home/gradle/src

# WORKDIR instruction sets the working directory for any RUN, CMD, ENTRYPOINT, COPY and ADD instructions
# that follow it in the Dockerfile.
# If the WORKDIR doesn't exist, it will be created even if it's not used in any subsequent Dockerfile instruction.
WORKDIR /home/gradle/src
# Debug command, see all logs/info into docker desktop logs
RUN ls
# Build command
RUN gradle build

LABEL org.name="malex"
# Package stage
FROM eclipse-temurin:21-jdk-jammy
# EXPOSE instruction informs Docker that the container listens on the specified network ports
# EXPOSE instruction doesn't actually publish the port.
EXPOSE 8080 5005
COPY --from=build /home/gradle/src/consumer/build/libs/docker_live_remote_debugging-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005","-jar","/app.jar"]