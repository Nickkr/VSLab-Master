# Build application
FROM maven:3.6.3-openjdk-11-slim as builder
WORKDIR /builds

COPY ./pom.xml ./pom.xml
RUN mvn verify clean --fail-never

COPY ./src ./src
RUN mvn clean package

# Package build application as executable docker image.
FROM openjdk:11-jre-slim
WORKDIR /services

ARG JAR_FILE=target/*.jar

ENV SPRING_PROFILES_ACTIVE docker
EXPOSE 18082

COPY ./config ./config
COPY --from=builder /builds/${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]
