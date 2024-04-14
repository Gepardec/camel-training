package com.gepardec.training.camel.best;

import org.apache.camel.EndpointInject;
import org.apache.camel.Predicate;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Test;

import com.gepardec.training.camel.commons.domain.ExampleOrders;
import com.gepardec.training.camel.commons.domain.OrderItem;
import com.gepardec.training.camel.commons.domain.OrderToProducer;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ChoiceTest extends CamelQuarkusTestSupport {

	private static final String TEST_NAME = "ChoiceTest";
	
    @Produce("direct:start" + TEST_NAME)
    private ProducerTemplate startProducer;

    @EndpointInject("mock:result" + TEST_NAME)
    private MockEndpoint resultEndpoint;

    @EndpointInject("mock:egg" + TEST_NAME)
    private MockEndpoint eggEndpoint;
    
    @EndpointInject("mock:other" + TEST_NAME)
    private MockEndpoint otherEndpoint;
    
    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() throws Exception {
                
                from("direct:start" + TEST_NAME + "1")  // OrderToProducer objects arrives
                	.log("Start with body: ${body}")
                	// TODO Insert choice that sends: 
                	//      orders with code OrderToProducer.EGG to eggEndpoint 
                	//      and all others to otherEndpoint
                	//      The original orders should also go to resultEndpoint
                	.to(resultEndpoint);

            }

        };
    }


    @Test
    public void test_egg_goes_to_egg_ep() throws InterruptedException {
        OrderToProducer msgIn = ExampleOrders.ORDER_TO_PRODUCER_EGGS;

        resultEndpoint.expectedBodiesReceived(msgIn);
        eggEndpoint.expectedBodiesReceived(msgIn);
        otherEndpoint.expectedMessageCount(0);

        startProducer.sendBody("direct:start" + TEST_NAME + "1", msgIn);
        
        resultEndpoint.assertIsSatisfied();
        eggEndpoint.assertIsSatisfied();
        otherEndpoint.assertIsSatisfied();
    }

    @Test
    public void test_milk_goes_to_other_ep() throws InterruptedException {
        OrderToProducer msgIn = ExampleOrders.ORDER_TO_PRODUCER_MILK;

        resultEndpoint.expectedBodiesReceived(msgIn);
        otherEndpoint.expectedBodiesReceived(msgIn);
        eggEndpoint.expectedMessageCount(0);

        startProducer.sendBody("direct:start" + TEST_NAME + "1", msgIn);
        
        resultEndpoint.assertIsSatisfied();
        eggEndpoint.assertIsSatisfied();
        otherEndpoint.assertIsSatisfied();
    }

}
