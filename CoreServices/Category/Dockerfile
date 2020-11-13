FROM openjdk:11-jre-slim

WORKDIR /services
ARG JAR_FILE=target/*.jar
ENV SPRING_PROFILES_ACTIVE docker
EXPOSE 18082

COPY config/application.yaml .
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]
