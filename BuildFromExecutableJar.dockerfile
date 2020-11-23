# Package build application as executable docker image.
FROM openjdk:11-jre-slim
WORKDIR /services

ARG FOLDER
ARG PORT
ARG JAR_FILE=target/*.jar

ENV SPRING_PROFILES_ACTIVE docker
EXPOSE ${PORT}

COPY ${FOLDER}/${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]
