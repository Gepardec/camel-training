package com.gepardec.training.camel.best;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ProcessorTest extends CamelQuarkusTestSupport {

    @Produce("direct:startProcessorTest")
    private ProducerTemplate startProducer;

    @EndpointInject("mock:resultProcessorTest")
    private MockEndpoint resultEndpoint;

    
    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() throws Exception {

                from("direct:startProcessorTest")
                	.log("start")
                	.process(new SimpleProcessor())
                	.log("end")
                	.to(resultEndpoint);                
            }
        };
    }

    @Test
    public void test_message_goes_from_in_to_out() throws InterruptedException {
        String msgIn = "Camel";
        String msgExpected = "Hello Camel";

        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody(msgIn);
        resultEndpoint.assertIsSatisfied();
    }

}
