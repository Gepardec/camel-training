Implement a route builder with FILE endpoint that
=====
- transforms OrderToProducer into a string 'key=value,key=value..' using a processor and sends a string into a file.
- uses filter to write out only the orders with amount > 100


#### Prerequisites
Maven, Java, completed exercise 11

##### Implement MeatOrderRouteBuilder that uses a file endpoint
Use following as a template
```
@ApplicationScoped
public final class MeatOrderRouteBuilder extends RouteBuilder {

    public static final String ENTRY_SEDA_ENDOINT_URI = "seda://meat_order_entry";
    public static final String OUTPUT_FILE_ENDPOINT_URI = "file://tmp.file";

    @Override
    public void configure() {

        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .handled(true);

        from(ENTRY_SEDA_ENDOINT_URI)
                .routeId(ENTRY_SEDA_ENDOINT_URI)
                .validate(...)
                .process(...)
                .to(OUTPUT_FILE_ENDPOINT_URI);
    }
}
```

##### Write Validator
Use lambda expression to validate body content
```
exchange -> exchange.getIn().getBody()...
```

##### Write Processor
It should transform java object `OrderToProducer` to a string like `parentId=.., amount=.., code=..`  
Use every kind of processor you like (java class, anonymous inner class, lambda). E.g. using lambda
```
exchange -> {
    OrderToProducer orderToProducer = exchange.getIn().getBody(OrderToProducer.class);
    StringBuilder builder = new StringBuilder();
    builder.append("parentId=").append(orderToProducer.getPartnerId())
            .append(", amount=").append(orderToProducer.getAmount())
            .append(", code=").append(orderToProducer.getCode());
    exchange.getIn().setBody(builder.toString());
}
```

##### Test the route using an integration test
Create an integration test by following template
```
package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.domain.OrderItem;
import com.gepardec.training.camel.commons.domain.OrderToProducer;
import com.gepardec.training.camel.commons.test.routetest.CamelRouteCDITest;
import com.gepardec.training.camel.commons.test.routetest.MockableEndpoint;
import com.gepardec.training.camel.commons.test.routetest.RouteId;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.Uri;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.cdi.Beans;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(CamelCdiRunner.class)
@Beans(classes = MeatOrderRouteBuilder.class)
public class MeatOrderRouteBuilderTest extends CamelRouteCDITest {

    @Inject
    @Uri("mock:result")
    @MockableEndpoint(MeatOrderRouteBuilder.OUTPUT_FILE_ENDPOINT_URI)
    @RouteId(MeatOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
    private MockEndpoint result;

    @Test
    public void testAmountUnder100_nothingInEndopoint(@Uri(MeatOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI) ProducerTemplate producer) throws InterruptedException {
        
    }

    @Test
    public void testAmountAbove100_EndopointContainsCorrectMessage(@Uri(MeatOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI) ProducerTemplate producer) throws InterruptedException {
        
    }
}

```
Implement `testAmountUnder100_nothingInEndopoint`. Create OrderToProducer object and send it to the entry endpoint.
```
OrderToProducer orderToProducer = new OrderToProducer(new OrderItem(OrderItem.MEAT, 80), 42);
producer.sendBody(orderToProducer);

```
Make an assertion and explain result.


Implement `testAmountAbove100_EndopointContainsCorrectMessage`. Create OrderToProducer object and send it to the entry endpoint.
```
OrderToProducer orderToProducer = new OrderToProducer(new OrderItem(OrderItem.MEAT, 120), 42);
producer.sendBody(orderToProducer);

```
Make an assertion and explain result.