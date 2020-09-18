# camel-trainig
The project for Camel Training

#### Technical hints

**Artemis Queue**
 
 ```
 docker run -it --rm -p 8161:8161 -p 61616:61616 -p 5672:5672 -e ARTEMIS_USERNAME=quarkus -e ARTEMIS_PASSWORD=quarkus vromero/activemq-artemis:2.9.0-alpine
```

**Postgres**

```
docker run -it --rm -p 5432:5432 -e POSTGRES_DB=quarkus -e POSTGRES_USER=quarkus -e POSTGRES_PASSWORD=quarkus postgres
```

```
mvn flyway:migrate
```

**Run Camel**
```
mvn camel:run
```