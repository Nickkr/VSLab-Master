# Package build application as executable docker image.
FROM tomcat:8.0
WORKDIR /services

ARG FOLDER="."
ARG PORT
ARG WAR_FILE="target/*.war"

EXPOSE ${PORT}

COPY ${FOLDER}/${WAR_FILE} /usr/local/tomcat/webapps/
COPY ./conf/tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml
