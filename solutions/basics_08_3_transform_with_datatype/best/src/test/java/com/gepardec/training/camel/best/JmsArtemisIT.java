package com.gepardec.training.camel.best;

import org.apache.camel.EndpointInject;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Test;

import com.gepardec.training.camel.commons.files.ExampleFiles;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class JmsArtemisIT extends CamelQuarkusTestSupport {

	private static final String TEST_NAME = "JmsArtemisTest";
	
    @Produce("direct:start" + TEST_NAME)
    private ProducerTemplate startProducer;

    @EndpointInject("mock:result" + TEST_NAME)
    private MockEndpoint resultEndpoint;

    
    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() throws Exception {

                from("direct:start" + TEST_NAME + "1")
                	.setExchangePattern(ExchangePattern.InOnly)
                	.to("jms://queue:eggs");
                
                from("jms://queue:eggs")
            		.to(resultEndpoint);
               
            }
        };
    }

    @Test
    public void test_write_and_read_queue() throws InterruptedException {
        String msgIn = ExampleFiles.ORDER_TO_PRODUCER_EGGS_XML;
        String msgExpected = msgIn;

        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody("direct:start" + TEST_NAME + "1", msgIn);
        resultEndpoint.assertIsSatisfied();
    }
}
