Basic Unit-Tests with Camel
===========================

Prerequisites: Exercise 05

Create a route that takes Order-Files from `src/orders` and sends it to `target/orders/processed`. 
Test this route with helt of a unit test that uses `CamelTestSupport` as base class.

Hints
-----

Add the following dependency to pom.xml:

```
   <dependency>
       <groupId>org.apache.camel</groupId>
       <artifactId>camel-test-cdi</artifactId>
       <scope>test</scope>
   </dependency>
```

Adapt the route:

```
package com.gepardec.trainings.camel.best;
  
import javax.inject.Inject;

import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.Uri;


public class MyRoutes extends RouteBuilder {

    public static final String URL_FILE_ORDERS_IN = "file:src/orders?noop=true";
    public static final String URL_FILE_ORDERS_OUT = "file:target/orders/processed";

    @Inject
    @Uri(URL_FILE_ORDERS_IN)
    private Endpoint inputEndpoint;

    @Inject
    @Uri(URL_FILE_ORDERS_OUT)
    private Endpoint resultEndpoint;

    @Override
    public void configure() {
        from(inputEndpoint)
        .to(resultEndpoint);
    }
}
```

and add a unit-test:

```
package com.gepardec.trainings.camel.best;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(CamelCdiRunner.class)
public class OrderProcessingTest extends CamelTestSupport {


    @Test
    public void when_order_in_orders_message_is_in_processed() throws InterruptedException {
        String orderIn = "{'partnerId': 1, 'items': [{ 'code': 1, 'amount': 110 }]}";

        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:result", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(1);

        template.sendBody("direct:start", orderIn);
        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("direct:start")
                .to(MyRoutes.URL_FILE_ORDERS_IN);

                from(MyRoutes.URL_FILE_ORDERS_OUT)
                .to("mock:result");
            }
        };
    }
}
```
