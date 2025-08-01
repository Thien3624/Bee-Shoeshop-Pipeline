FROM maven:3.9.9-eclipse-temurin-17-alpine AS build

WORKDIR /app
COPY . .
RUN mvn install -DskipTests=true

FROM openjdk:17-jdk-alpine
COPY --from=build /app/target/server-0.0.1-SNAPSHOT.jar /run/server-0.0.1-SNAPSHOT.jar

ENTRYPOINT java -jar /run/server-0.0.1-SNAPSHOT.jar

