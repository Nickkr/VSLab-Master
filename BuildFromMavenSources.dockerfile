# Build application
FROM maven:3.6.3-openjdk-11-slim as builder
WORKDIR /builds

ARG FOLDER
ARG PORT

ARG MAVEN_OPTIONS="--batch-mode --quiet --fail-fast --strict-checksums --threads 1C --define skipTests"

COPY ${FOLDER}/pom.xml ./pom.xml
RUN mvn ${MAVEN_OPTIONS} dependency:go-offline

COPY ${FOLDER}/src ./src
RUN mvn --offline ${MAVEN_OPTIONS} clean package

# Package build application as executable docker image.
FROM openjdk:11-jre-slim
WORKDIR /services

ARG PORT
ARG JAR_FILE="target/*.jar"

ENV SPRING_PROFILES_ACTIVE="docker"
EXPOSE ${PORT}

COPY --from=builder /builds/${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]
