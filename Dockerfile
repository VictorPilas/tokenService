FROM openjdk:17-jdk-slim

EXPOSE 8081

COPY target/tokenService-0.0.1-SNAPSHOT.jar token-service.jar

ENTRYPOINT ["java","-jar","/token-service.jar"]