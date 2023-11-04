FROM openjdk:17-alpine

# Install curl
RUN apk add --no-cache curl


COPY build/ /app/build/.

CMD ["tail", "-f", "/dev/null"]

ENTRYPOINT  ["java", "-jar", "/app/build/libs/products-api-1.0-SNAPSHOT.jar"]