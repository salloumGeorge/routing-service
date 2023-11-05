FROM openjdk:17-alpine

# Install curl
RUN apk add --no-cache curl

expose 8085

VOLUME /tmp

ARG JAR_FILE=build/libs/billing-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} app.jar

ENTRYPOINT  ["java", "-jar", "/app.jar"]