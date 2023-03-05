# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /backend

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN chmod +x mvnw \
    && ./mvnw dependency:resolve

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]