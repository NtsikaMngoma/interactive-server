# InteractiveServer

This application was built using Springboot 2.2.5-RELEASE, you can find documentation and help at [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot).

This is a "microservice" application intended to be part of a microservice architecture, please refer to the Spring REST and Spring Data JPA Projects for more information.

This application is configured for Service Discovery and Configuration with . On launch, it will refuse to start if it is not able to connect to required services.

## Development

To start your application in the dev profile, run:

    ./mvnw or mvn clean package/install if maven is installed locally

## Building for production

### Packaging as jar

To build the final jar and optimize the InteractiveServer application for production, run:

    ./mvnw -Pprod clean verify

To ensure everything worked, run:

    java -jar target/*.jar

Refer to https://spring.io/blog/2014/03/07/deploying-spring-boot-applications for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

    ./mvnw -Pprod,war clean verify

## Testing

To launch your application's tests, run:

    ./mvnw verify

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar
```

## Using Docker to simplify development (optional)

You can use Docker to improve your Software Development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a mssql database in a docker container, run:

    docker-compose -f src/main/docker/mssql.yml up -d

To stop it and remove the container, run:

    docker-compose -f src/main/docker/mssql.yml down

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

    ./mvnw -Pprod verify jib:dockerBuild

Then run:

    docker-compose -f src/main/docker/app.yml up -d

For more information refer to https://docs.docker.com/compose/gettingstarted/, this page also contains information on the docker-compose sub-generator, which is able to generate docker configurations for one or several Spring applications.

