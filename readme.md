# Verteile Systeme Master Laboratory

*WebShop implementation with microservices.*

## Run the application using docker compose

You can do the build and run in one command.

~~~bash
docker-compose up --build --detach
~~~

Or follow the detailed instructions:

1. *Optionally* if you want to predownload used docker images, do a pull first.

    ~~~bash
    docker pull openjdk:11-jre-slim
    docker pull openjdk:11-slim
    docker pull maven:3.6.3-openjdk-11-slim
    ~~~

2. Build the services

    ~~~bash
    docker-compose build
    ~~~

3. *Optionally* delete the local MySql data for a fresh database initialization.

    ~~~bash
    rm -rf .data
    ~~~

4. Run the services

    ~~~bash
    docker-compose up --detach
    ~~~

## API Documentation of the WebShop

The API is modeled with the RESTful API Modeling Language [RAML](https://raml.org/). The file `api.raml` contains the documentation of the api.

### Generating the API-Documentation

There are **two** ways to generate the API-Documentation

1. Docker
    * Start `docker-compose up api-docs`
    * Open (<http://localhost:8080>) to view the documentation
2. raml2html
    * `cd documentation`
    * *(first time)* `npm install`
    * `npm run build` or `npm run build:watch` to automatically build on changes
    * open the generated file `api-docs.html`

## Initial development setup

### Build Requirements

For local development you should have

* the [JDK 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
* and [Apache Maven 3.6.3](https://maven.apache.org/index.html) installed.

Also check if your `JAVA_HOME` environment variable is configured correctly.

### Generate some inital code

You can generate an initial Spring Boot Core project using [Spring Initializr](https://start.spring.io/).

* Open the [pre-configured example](https://start.spring.io/#!type=maven-project&language=java&platformVersion=2.4.0.RELEASE&packaging=jar&jvmVersion=11&groupId=com.core&artifactId=category-service&name=category-service&description=Category%20Core%20Service&packageName=com.core.category-service&dependencies=web,devtools,actuator,mysql,data-jpa,cloud-eureka,oauth2-client) and define your customization.

Follow the [Guides](#guides) to create some inital code.

### Start your code

To run your application locally, goto to your working directory containing the pom.xml and:

1. Start the database.

    ~~~bash
    docker-compose up --detach db
    ~~~

2. Start the application using

    ~~~bash
    mvn spring-boot:run
    ~~~

3. Check the console output, to see on which port Tomcat has started.

4. Open localhost on this port `http://localhost:$PORT/greeting?name=User` and check for a response.

## Further guides and tutorials

Spring Data JPA implement filtering with optional parameters:

* [Spring @RequestParam Annotation](https://www.baeldung.com/spring-request-param)
* [Spring Data JPA Query by Example](https://www.baeldung.com/spring-data-query-by-example)
* [Spring Data Repositories Query Methods](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods)

Creating the composite service and call core services:

* [Spring 5 WebClient](https://www.baeldung.com/spring-5-webclient)
* [Spring WebClient – GET, PUT, POST, DELETE examples](https://howtodoinjava.com/spring-webflux/webclient-get-post-example/)
* *deprecated for our used Spring version (5.2.10 / 5.3.1)*
  * [Consuming a RESTful Web Service](https://spring.io/guides/gs/consuming-rest/)
  * [The Guide to RestTemplate](https://www.baeldung.com/rest-template)

Eureka-Ribbon Discovery:

* [Ribbon - Loadblanced](http://www.masterspringboot.com/cloud/netflix/service-discovery-with-netflix-eureka-and-ribbon-client-load-balancer)

More development related articles:

* [Advanced Microservices Security with Spring and OAuth2](https://dzone.com/articles/advanced-microservices-security-with-spring-and-oa)
* [How to Use Docker’s Health Check Command](https://scoutapm.com/blog/how-to-use-docker-healthcheck)

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.0/maven-plugin/reference/html/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.4.0/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.4.0/reference/htmlsingle/#using-boot-devtools)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.4.0/reference/htmlsingle/#production-ready)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.4.0/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [OAuth2 Client](https://docs.spring.io/spring-boot/docs/2.4.0/reference/htmlsingle/#boot-features-security-oauth2-client)
* [Spring Cloud Netflix Eureka](https://cloud.spring.io/spring-cloud-netflix/reference/html/)

### Guides

The following guides illustrate how to use some features concretely:

* [Getting Started Guides](https://spring.io/guides)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
