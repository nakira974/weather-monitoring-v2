# Use Java 17 as the base image
FROM openjdk:17-jdk-alpine3.14

WORKDIR /app
CMD mkdir /app/certs
VOLUME /app/certs

COPY build/libs/*.jar /app/
COPY src/main/resources/application.properties /app/
EXPOSE 8083

CMD ["java", "-jar", "weather-monitoring-v2-0.0.1-SNAPSHOT.jar"]