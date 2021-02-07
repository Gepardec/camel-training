# camel-trainig
The project for Camel Training

#### Technical hints

**Artemis Queue**

Open new command-line and run
 ```
 docker run -it --rm -p 8161:8161 -p 61616:61616 -p 5672:5672 -e ARTEMIS_USERNAME=camel -e ARTEMIS_PASSWORD=camel vromero/activemq-artemis:2.9.0-alpine
```

**Postgres**

Open new command-line and run
```
docker run -it --rm -p 5432:5432 -e POSTGRES_DB=camel -e POSTGRES_USER=camel -e POSTGRES_PASSWORD=camel postgres
```

In separate command-line run
```
cd ./solutions/commons
mvn flyway:migrate
cd ../..
```

**Run Camel**
Open new command-line and run
```
cd solutions/best
mvn camel:run
```

**Run Integration Tests**

In separate command-line run
```
cd solutions/best
mvn -Dmaven.failsafe.skip=false failsafe:integration-test
```
