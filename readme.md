# Verteile Systeme Master Laboratory

*WebShop implementation with microservices.*

## Run the application using docker compose

You can do the build and run in one command.

~~~bash
docker-compose up --build --detach
~~~

### Using local compiled executables during development

When you have maven installed on your machine, you can configure the docker-compose build to use existing java executables. These java executables needs to be compiled with maven before you running docker-compose.

You can use the enviroment variable `BUILD_FROM` to select which dockerfile is used for the build.
If you obmit this variable, then the build from maven sources is used by default.

* with `BUILD_FROM=MavenSources` the source code is compiled during the docker build using the `maven:3.6.3-openjdk-11-slim` image. Therefore maven needs to download all dependencies, as it cannot reuse the hosts local repository. This might increase your build time.

    ~~~bash
    BUILD_FROM=MavenSources docker-compose build
    ~~~

* with `BUILD_FROM=ExecutableJar` the pre-compiled jar executable is packed into the `openjdk:11-jre-slim` image. This requires you to compile the sources on the host system before the docker build.

    ~~~bash
    mvn clean package -DskipTests
    BUILD_FROM=ExecutableJar docker-compose build
    ~~~

### Build and Startup script

You also can use the `package-build-up.cmd` script for compiling the sources with your local maven, building the docker images and start the services.

* The maven pom in your working directory is used for compilation.
* The first argument is passed to maven a artifactId to select the builded project, even if your working directory pom contains other projects.
* The following arguments are passed to `docker-compose up` for starting other services, too.
* You can pass the same arguments as for `docker-compose build` and `docker-compose up`, to specify a service.

Example usage:

* Run it in your project folder without arguments, to build and start the whole application:

    ~~~cmd
    package-build-up.cmd
    ~~~

* Pass a service name to build and start this specific service, only.
  *Its dependend services are included automatically by docker.*

    ~~~cmd
    package-and-up.cmd category-service
    ~~~

* Pass a second service name to start this service, too. This only rebuild the category service, but starts the whole application.

    ~~~cmd
    package-and-up.cmd category-service zuul-server
    ~~~

### Detailed build instructions

1. *Optionally* if you want to predownload used docker images, do a pull first.

    ~~~bash
    docker pull openjdk:11-jre-slim
    docker pull openjdk:11-slim
    docker pull maven:3.6.3-openjdk-11-slim
    ~~~

2. Compile the sources

    ~~~bash
    mvn clean package -DskipTests
    ~~~

3. Build the services

    ~~~bash
    BUILD_FROM=ExecutableJar docker-compose build
    ~~~

4. *Optionally* removes containers, networks and the local MySql data storage volume for a fresh database initialization.

    * Pass the `--rmi local` to remove local images, from previous executions.
    * Pass the `--remove-orphans` to remove undefined service, from previous developments.

    ~~~bash
    docker-compose down --volumes --rmi local --remove-orphans
    ~~~

5. Run the services

    ~~~bash
    docker-compose up --detach
    ~~~

## Usage links

The Webshop API is provided by the Zuul server at <http://localhost:8081/webshop-api/>.

Open the following links to view:

* API-documentation <http://localhost:8080/>
* Registered instances at Eureka server <http://localhost:8761/>
* Hystrix Dashboard at Zuul server <http://localhost:8081/hystrix>
* Monitor the Webshop API with Hystrix <http://localhost:8081/hystrix/monitor?stream=http%3A%2F%2Flocalhost%3A8081%2Fturbine.stream&title=Webshop%20API>

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

* Open the [pre-configured example](https://start.spring.io/#!type=maven-project&language=java&platformVersion=2.3.6.RELEASE&packaging=jar&jvmVersion=11&groupId=com.core&artifactId=category-service&name=category-service&description=Category%20Core%20Service&packageName=com.core.category-service&dependencies=web,devtools,actuator,mysql,data-jpa,cloud-eureka) and define your customization.

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

## Authorization

### Links

[Keys](http://localhost:18085/auth/oauth2/keys)
[Token](http://messaging-client:secret@localhost:18085/oauth/token)

Body-Parameter für Token: 

grant_type: password
username: admin
password: password
scope: message.read

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
* [The Mystery of Eureka Health Monitoring](https://dzone.com/articles/the-mystery-of-eureka-health-monitoring)

Microservice Security mit OAuth2:

* [Spring Security OAuth2 Boot](https://docs.spring.io/spring-security-oauth2-boot/docs/current/reference/html5/)
* [Microservices Security with OAuth2](https://piotrminkowski.wordpress.com/2017/02/22/microservices-security-with-oauth2/)
* [Advanced Microservices Security with Spring and OAuth2](https://dzone.com/articles/advanced-microservices-security-with-spring-and-oa)
* [OAuth2 configure Authorization and Resource server](https://stackoverflow.com/a/44252949)

More development related articles:

* [How to Use Docker’s Health Check Command](https://scoutapm.com/blog/how-to-use-docker-healthcheck)

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.3.6.RELEASE/maven-plugin/reference/html/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.3.6.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.3.6.RELEASE/reference/htmlsingle/#using-boot-devtools)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.3.6.RELEASE/reference/htmlsingle/#production-ready)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.3.6.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [OAuth2 Client](https://docs.spring.io/spring-boot/docs/2.3.6.RELEASE/reference/htmlsingle/#boot-features-security-oauth2-client)
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
