FROM ubuntu:latest
LABEL authors="maxim"

FROM mcr.microsoft.com/java/jdk:11-zulu-alpine

COPY target/api.jar /app/api.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/api.jar"]