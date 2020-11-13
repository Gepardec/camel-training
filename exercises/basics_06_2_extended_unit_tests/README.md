Extended CDI-Unit-Tests with Camel
===========================

Prerequisites: Exercise 06

Create a unit test to an existing route using commons-test tooling and mocking real endpoints.

Hints
-----

##### Build commons and commons-test via `mvn clean install`
##### Replace the following dependency to pom.xml:

```
<dependency>
   <groupId>org.apache.camel</groupId>
   <artifactId>camel-test-cdi</artifactId>
   <scope>test</scope>
</dependency>
```

with 
```
<dependency>
    <groupId>com.gepardec.training.camel</groupId>
    <artifactId>commons-test</artifactId>
    <version>${project.version}</version>
    <scope>test</scope>
</dependency>
```
##### Adapt the class MyRoutes by adding the routeId:

```
package com.gepardec.trainings.camel.best;

import javax.inject.Inject;

import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ImportResource;
import org.apache.camel.cdi.Uri;

public class MyRoutes extends RouteBuilder {

    public static final String URL_FILE_ORDERS_IN = "file://src/orders?noop=true";
    public static final String URL_FILE_ORDERS_OUT = "file://target/orders/processed";

    @Inject
    @Uri(URL_FILE_ORDERS_IN)
    private Endpoint inputEndpoint;

    @Inject
    @Uri(URL_FILE_ORDERS_OUT)
    private Endpoint resultEndpoint;

    @Override
    public void configure() {
        from(inputEndpoint).routeId("MyRoutes")
                .to(resultEndpoint);
    }
}
```

and add a unit-test:

```
package com.gepardec.trainings.camel.best;

import com.gepardec.training.camel.commons.test.routetest.CamelRouteCDITest;
import com.gepardec.training.camel.commons.test.routetest.MockableEndpoint;
import com.gepardec.training.camel.commons.test.routetest.RouteId;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.Uri;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.cdi.Beans;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.*;

@RunWith(CamelCdiRunner.class)
@Beans(classes = MyRoutes.class)
public class MyRoutesTest extends CamelRouteCDITest {

    //replace resultEndpoint of MyRoutes with mock endpoint
    @Inject
    @Uri("mock:result")
    @MockableEndpoint(MyRoutes.URL_FILE_ORDERS_OUT)
    @RouteId("MyRoutes")
    private MockEndpoint result;

    //replace input file endpoint with a direct endpoint, simulating file input
    @Inject
    @Uri("direct:fileinput")
    @MockableEndpoint(MyRoutes.URL_FILE_ORDERS_IN)
    @RouteId("MyRoutes")
    private ProducerTemplate fileInputProducer;

    @Test
    public void testRoute() throws InterruptedException {
        // set expected message count
        result.expectedMessageCount(1);

        // simulate file input
        fileInputProducer.sendBody("My test content");

        // assert number of messages
        result.assertIsSatisfied();

        // get exchange
        Exchange exchange = result.getExchanges().get(0);

        // assert exchange content (simulated output to file)
        Assertions.assertThat(exchange.getIn().getBody(String.class)).isEqualTo("My test content");
    }
}
```
