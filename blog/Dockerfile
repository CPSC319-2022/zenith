# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy as base
WORKDIR /backend
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw \
    && ./mvnw dependency:resolve
COPY src ./src

FROM base as test
RUN ["./mvnw", "test"]

FROM test as build
RUN ./mvnw package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-jammy as production
COPY --from=build /backend/target/backend.jar ./
CMD ["java", "-jar", "backend.jar"]
EXPOSE 8080 



