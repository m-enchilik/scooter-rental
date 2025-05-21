FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app1

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY scooter-rental-api/ ./scooter-rental-api
COPY scooter-rental-application/ ./scooter-rental-application
COPY scooter-rental-core/ ./scooter-rental-core
COPY scooter-rental-db/ ./scooter-rental-db

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /app1

COPY --from=build /app1/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
