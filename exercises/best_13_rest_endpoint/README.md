Provide REST Endpoint and implement integration tests
===================

#### Prerequisites
Maven, Java, completed exercise 12

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
### Exercise
##### Add Support for Camel-Rest (not needed if commons is used):
```
<dependency>
    <groupId>org.apache.camel</groupId>
    <artifactId>camel-rest</artifactId>
</dependency>
```

##### Examine how web binding for rest endpoint is configured
See `com.gepardec.training.camel.commons.config.RestConfigurator`

##### Implement a route exposing a REST Endpoint bound on http://localhost:8080/best
Use following as a template
```
package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.domain.Order;
import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
public final class EntryRouteBuilder extends RouteBuilder {

    @Override
    public void configure() {

        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .handled(true);
    }
}

```
Using examples from https://camel.apache.org/manual/latest/rest-dsl.html accomplish following steps:

 - Configure the path by calling `rest("/best/")`
 - Configure support of POST by attaching `.post()`
 - Configure consuming of JSON by attaching `.consumes(MediaType.APPLICATION_JSON)`
 - Configure deserialization to Order by attaching `.type(Order.class)`
 - Configure forwarding the Order object to splitter by attaching `.to(SplitterRouteBuilder.SPLITTER_FROM_ENDOINT_URI)`
 - Set HTTP Status ACCEPTED to the response by attaching `.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(Response.Status.ACCEPTED.getStatusCode()))`
 - Finish route by `.endRest();`

##### Build and run BEST
```
mvn clean install
mvn camel:run
```

##### Start Database using corresponding docker command and perform flyway migration

##### Implement an integration test for the pasta logic
Create a file test/resources/json/order_pasta.json with following content
```
{
  "partnerId": 1,
  "items": [{
    "code": 2,
    "amount": 120
  }]
}
```

Create an integration test by following template
```
package com.gepardec.trainings.camel.best;

import com.gepardec.training.camel.commons.config.DbConnection;
import com.gepardec.training.camel.commons.test.integrationtest.CamelIntegrationTest;
import com.gepardec.training.camel.commons.test.integrationtest.RestServiceTestSupport;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import org.assertj.db.type.Table;
import org.junit.Test;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

import static com.ninja_squad.dbsetup.operation.CompositeOperation.sequenceOf;
import static org.assertj.db.api.Assertions.assertThat;

public class PastaOrderRouteBuilderIT extends CamelIntegrationTest {

    private static final String PASTA_JSON_FILE_PATH = "json/order_pasta.json";

    @Test
    public void correctInputJson_CorrectDBEntryIsCreated() throws IOException, SQLException {
        clearDB();
    }

    private void clearDB() throws IOException, SQLException {
        Operation operation =
                sequenceOf(
                        deleteAllFrom("order_to_producer"));
        DataSource dataSource = DbConnection.getDatasource();
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();

    }


}
```
Read json content from the file and send it to the rest endpoint
```
String json = getFileAsString(PASTA_JSON_FILE_PATH);
RestServiceTestSupport.callPost("", json, 202);
```

Create a connection to the database table
```
Table table = new Table(DbConnection.getDatasource(), "order_to_producer");
```

Make an assertion, that the correct entry is created
```
assertThat(table).hasNumberOfRows(1)
        .column("partner_id").value().isEqualTo(2)
        .column("item_code").value().isEqualTo(2)
        .column("item_count").value().isEqualTo(120);
```

Execute test and explain results.

Try to get the test green.