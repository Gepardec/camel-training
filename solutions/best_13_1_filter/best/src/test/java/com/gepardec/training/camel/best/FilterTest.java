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
            	
				validator().type("myjava:order2producer").withJava(Order2ProducerValidator.class);
								
            	
                from("direct:start" + TEST_NAME + "1")
                	.setHeader("bar", constant(110))
                	.filter(header("bar").isGreaterThan(100))
                		.to(resultEndpoint);
                            
                from("direct:start" + TEST_NAME + "3")
            		.filter(bodyAs(String.class).regex("Hello \\d you!"))
            			.to(resultEndpoint);

                from("direct:start" + TEST_NAME + "4")
            		.filter(e -> e.getIn().getBody(OrderToProducer.class).getAmount() > 100)
            			.setBody(constant("OK"))
            			.to(resultEndpoint)
            		.end();
            	

            }
        };
    }

    @Test
    public void test_filter_header() throws InterruptedException {
        String msgIn = "Hello for you!";
        String msgExpected = msgIn;

        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody("direct:start" + TEST_NAME + "1", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void test_filter_body_regex() throws InterruptedException {
        String msgIn = "Hello 2 you!";
        String msgExpected = msgIn;

        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody("direct:start" + TEST_NAME + "3", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void test_filter_body_regex_failed() throws InterruptedException {
        String msgIn = "Hello for you!";

        resultEndpoint.expectedMessageCount(0);

        startProducer.sendBody("direct:start" + TEST_NAME + "3", msgIn);
        resultEndpoint.assertIsSatisfied();
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
