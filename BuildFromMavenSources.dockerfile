# Build application
FROM maven:3.6.3-openjdk-11-slim as builder
WORKDIR /builds

ARG FOLDER
ARG PORT

COPY ${FOLDER}/pom.xml ./pom.xml
RUN mvn verify clean --fail-never

COPY ${FOLDER}/src ./src
RUN mvn clean package -DskipTests

# Package build application as executable docker image.
FROM openjdk:11-jre-slim
WORKDIR /services

ARG PORT
ARG JAR_FILE=target/*.jar

ENV SPRING_PROFILES_ACTIVE docker
EXPOSE ${PORT}

COPY --from=builder /builds/${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]
