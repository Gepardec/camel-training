package com.gepardec.trainings.camel.best;

import java.lang.reflect.Field;

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.Uri;
import org.apache.camel.component.direct.DirectEndpoint;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.reifier.RouteReifier;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(CamelCdiRunner.class)
public class OrderProcessingTest extends CamelTestSupport {

    @Inject
    protected CamelContext ctx;
	
    @Test
    public void when_order_in_orders_message_is_in_processed() throws InterruptedException {
        String orderIn = "{\"partnerId\": 1, \"items\": [{ \"code\": 1, \"amount\": 110 }]}";
        String orderExpected = "{\"partnerId\":34,\"items\":[{\"code\":1,\"amount\":110}]}";

        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:result", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(orderExpected);
        
        
        template.sendBody("direct:start", orderIn);
        resultEndpoint.assertIsSatisfied();
    }
/*
    @Test
    public void when_order_in_orders_message_is_in_direct_entpoint() throws Exception {
    	        
    	String orderIn = "{'partnerId': 1, 'items': [{ 'code': 1, 'amount': 110 }]}";

    	AdviceWithRouteBuilder.adviceWith(ctx, "orderIn", in -> {
            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                    in.weaveById("direct_order_in")
                            .replace()
                            .to("direct:mocked_in");

                }
            });
       getMockEndpoint("mock:direct_order_in").expectedMessageCount(1);
        
        
        
        template.sendBody(MyRoutes.URL_FILE_ORDERS_IN, orderIn);
        assertMockEndpointsSatisfied();
    }
*/
    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("direct:start")
                .log("Send to file order: ${body}")
                .to(MyRoutes.URL_FILE_ORDERS_IN);
                               
                from(MyRoutes.URL_FILE_ORDERS_OUT)
                .to("mock:result");
            }
        };
    }
 
    @Override
    public String isMockEndpoints() {
        // override this method and return the pattern for which endpoints to mock.
        // use * to indicate all
        return "*";
    }
}