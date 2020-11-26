Implement a route builder with SQL endpoint that saves OrderToProducer in the corresponsing database table.
===================

#### Prerequisites
Maven, Java, completed exercise 10

#### Technical hints

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

### Exercise
##### Add Support for Camel-Sql
```
<dependency>
    <groupId>org.apache.camel</groupId>
    <artifactId>camel-jdbc</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.camel</groupId>
    <artifactId>camel-sql</artifactId>
</dependency>
```
##### Add Support for Agroal to provide datasources
```
<dependency>
    <groupId>io.agroal</groupId>
    <artifactId>agroal-api</artifactId>
</dependency>
<dependency>
    <groupId>io.agroal</groupId>
    <artifactId>agroal-pool</artifactId>
</dependency>
```
##### Add Support for Postgres
```
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
```

##### Examine how datasources are configured
 - `commons/src/main/resources/application.properties`
 - `com.gepardec.training.camel.commons.config.DbConnection`

##### Implement a bean that provides postgresDatasource
Use following as a template
```
@Produces
@Named("postgresDatasource")
public DataSource createPostgresDatasource() throws IOException, SQLException {
    return DbConnection.getDatasource();
}
```

##### Implement PastaOrderRouteBuilder that uses an sql endpoint
Use following as a template
```
@ApplicationScoped
public final class PastaOrderRouteBuilder extends RouteBuilder {

    public static final String ENTRY_SEDA_ENDOINT_URI = "seda://pasta_order_entry";
    public static final String OUTPUT_SQL_ENDPOINT_URI = "sql://<SQL-QUERY>?dataSource=#postgresDatasources";


    @Inject
    @Uri(ENTRY_SEDA_ENDOINT_URI)
    private Endpoint entryEndpoint;

    @Inject
    @Uri(OUTPUT_SQL_ENDPOINT_URI)
    private Endpoint sqlEndpoint;

    @Override
    public void configure() throws IOException, SQLException {

        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .handled(true);

        from(entryEndpoint).routeId(ENTRY_SEDA_ENDOINT_URI)
                .to(sqlEndpoint);
    }

}
```

##### Create the SQL-Query
Template:
```
insert into order_to_producer (id, partner_id, item_code, item_count) values (<ID>, <PARTNER_ID>, <ITEM_CODE>, <ITEM_AMOUNT>)
```

In order to pass the values from body, we can use placeholders:
 - `:#${body.partnerId}`
 - `:#${body.code}`
 - `:#${body.amount})`
 
The database table requires UUID in the column _id_. So we need to implement a UUID generator and pass generated ids into the query.
 - `:#${bean:uuidGenerator.nextId}`
 
Inspect the provided UUID-Generator: `com.gepardec.training.camel.commons.misc.IdGenerator`
 
Configure UUID-Generator bean:
```
@Produces
@Named("uuidGenerator")
public IdGenerator createIdGenerator() {
    return new IdGenerator();
}
```

##### Test the route using an integration test
Create an integration test by following template
```
package com.gepardec.training.camel.best;

import com.gepardec.training.camel.best.db.CommonOperations;
import com.gepardec.training.camel.commons.config.DbConnection;
import com.gepardec.training.camel.commons.domain.OrderItem;
import com.gepardec.training.camel.commons.domain.OrderToProducer;
import com.gepardec.training.camel.commons.test.routetest.CamelRouteCDITest;
import com.gepardec.training.camel.commons.test.routetest.MockableEndpoint;
import com.gepardec.training.camel.commons.test.routetest.RouteId;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.Uri;
import org.apache.camel.test.cdi.Beans;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.assertj.db.type.Table;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

import static com.ninja_squad.dbsetup.operation.CompositeOperation.sequenceOf;
import static org.assertj.db.api.Assertions.assertThat;

@RunWith(CamelCdiRunner.class)
@Beans(classes = PastaOrderRouteBuilder.class)
public class PastaOrderSqlIT extends CamelRouteCDITest {

    @Inject
    @Uri("direct:triggerSql")
    @MockableEndpoint(PastaOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
    @RouteId(PastaOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
    private ProducerTemplate entryEndpoint;

    @Test
    public void correctInput_CorrectEntryIsCreated() throws IOException, SQLException {
        clearDB();
    }

    private void clearDB() throws IOException, SQLException {
        Operation operation =
                sequenceOf(
                        CommonOperations.DELETE_ALL);
        DataSource dataSource = DbConnection.getDatasource();
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();

    }
}
```
Create OrderToProducer object and send it to the entry endpoint
```
OrderToProducer orderToProducer = new OrderToProducer(new OrderItem(OrderItem.PASTA, 120), 2);
entryEndpoint.sendBody(orderToProducer);

```

Create a connection to the database table
```
Table table = new Table(DbConnection.getDatasource(), "order_to_producer");
```

Make an assertion, that the correct entry is created
```
assertThat(table).hasNumberOfRows(1)
        .column("partner_id").value().isEqualTo(1)
        .column("item_code").value().isEqualTo(1)
        .column("item_count").value().isEqualTo(110);
```

Execute test and explain results.

Try to get the test green.