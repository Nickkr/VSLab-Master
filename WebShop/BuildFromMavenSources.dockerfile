# Build application
FROM maven:3.5.4-jdk-8-alpine as builder
WORKDIR /builds

ARG FOLDER="."
ARG PORT

ARG MAVEN_OPTIONS="--batch-mode --fail-fast --strict-checksums --threads 1C --define skipTests"

COPY ${FOLDER}/pom.xml ./pom.xml
RUN mvn ${MAVEN_OPTIONS} dependency:go-offline

COPY ${FOLDER}/src ./src
RUN mvn ${MAVEN_OPTIONS} clean package

# Package build application as executable docker image.
FROM tomcat:8.0
WORKDIR /services

ARG PORT
ARG WAR_FILE="target/*.war"

EXPOSE ${PORT}

COPY --from=builder /builds/${WAR_FILE} /usr/local/tomcat/webapps/
COPY ./conf/tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml
