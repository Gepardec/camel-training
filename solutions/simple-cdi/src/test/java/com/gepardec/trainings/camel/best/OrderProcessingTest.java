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