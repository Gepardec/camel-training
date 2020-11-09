# camel-trainig
The project for Camel Training

#### Technical hints

**Artemis Queue**
 
 ```
 docker run -it --rm -p 8161:8161 -p 61616:61616 -p 5672:5672 -e ARTEMIS_USERNAME=camel -e ARTEMIS_PASSWORD=camel vromero/activemq-artemis:2.9.0-alpine
```

**Postgres**

```
docker run -it --rm -p 5432:5432 -e POSTGRES_DB=camel -e POSTGRES_USER=camel -e POSTGRES_PASSWORD=camel postgres
```

```
cd ./solutions/commons
```

```
mvn flyway:migrate
```

**Run Camel**
```
mvn camel:run
```

**Run Integration Tests**
```
mvn -Dmaven.failsafe.skip=false failsafe:integration-test
```
