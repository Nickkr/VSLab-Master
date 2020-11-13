# Verteile Systeme Master Laboratory

## Predownload used docker images

docker pull openjdk:11-jre-slim
docker pull openjdk:11-slim
docker pull maven:3.6.3-openjdk-11-slim

https://registry.hub.docker.com/v1/repositories/openjdk/tags
wget -q --no-check-certificate https://registry.hub.docker.com/v1/repositories/openjdk/tags -O -  | sed -e 's/[][]//g' -e 's/"//g' -e 's/ //g' | tr '}' '\n'  | awk -F: '{print $3}' | grep ^8-j
wget -q --no-check-certificate https://registry.hub.docker.com/v1/repositories/maven/tags -O -  | sed -e 's/[][]//g' -e 's/"//g' -e 's/ //g' | tr '}' '\n'  | awk -F: '{print $3}' | grep 3.6.3 | grep 11


## Generate an initial Spring Boot Core project

https://start.spring.io/#!type=maven-project&language=java&platformVersion=2.4.0.RELEASE&packaging=jar&jvmVersion=11&groupId=com.core&artifactId=category-service&name=category-service&description=Category%20Core%20Service&packageName=com.core.category-service&dependencies=web,devtools,actuator,mysql,data-jpa,cloud-eureka,oauth2-client

You need to have the JDK 11 and maven installed. Also check if your JAVA_HOME environment is configured correctly.

Follow the https://spring.io/guides/gs/rest-service/ tutorial to create inital code.

Start the application using
~~~bash
  cd CoreServices\Category
  mvn spring-boot:run
~~~

Open http://localhost:8080/greeting?name=User to check the response.


## Further guides and tutorials


* https://spring.io/guides
* https://cloud.spring.io/spring-cloud-netflix/reference/html/
* https://dzone.com/articles/advanced-microservices-security-with-spring-and-oa



### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.0/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.4.0/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.4.0/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.4.0/reference/htmlsingle/#using-boot-devtools)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.4.0/reference/htmlsingle/#production-ready)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.4.0/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [OAuth2 Client](https://docs.spring.io/spring-boot/docs/2.4.0/reference/htmlsingle/#boot-features-security-oauth2-client)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)


