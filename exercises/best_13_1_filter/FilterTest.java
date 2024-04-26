package com.gepardec.training.camel.best;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Test;

import com.gepardec.training.camel.commons.domain.ExampleOrders;
import com.gepardec.training.camel.commons.domain.OrderToProducer;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class FilterTest extends CamelQuarkusTestSupport {

	private static final String TEST_NAME = "FilterTest";
	
    @Produce("direct:start" + TEST_NAME)
    private ProducerTemplate startProducer;

    @EndpointInject("mock:result" + TEST_NAME)
    private MockEndpoint resultEndpoint;

    
    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() throws Exception {
            	
                from("direct:start" + TEST_NAME + "4")
          // TODO Insert Filter where amount > 100
            			.setBody(constant("OK"))
            			.to(resultEndpoint)
            		.end();
            	

            }
        };
    }

    @Test
    public void test_filter_order_ok() throws InterruptedException {
        OrderToProducer msgIn = ExampleOrders.ORDER_TO_PRODUCER_MEAT;
        String msgExpected = "OK";

        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody("direct:start" + TEST_NAME + "4", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void test_filter_order_failed() throws InterruptedException {
        OrderToProducer msgIn = ExampleOrders.ORDER_TO_PRODUCER_MEAT_90;

        resultEndpoint.expectedMessageCount(0);

        startProducer.sendBody("direct:start" + TEST_NAME + "4", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

}
