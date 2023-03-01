FROM maven:3.8.6
#FROM openjdk:11
WORKDIR /backend
COPY / .
RUN mvn build
ENV PORT=8080
EXPOSE 8080
CMD ["mvn", "build"]

