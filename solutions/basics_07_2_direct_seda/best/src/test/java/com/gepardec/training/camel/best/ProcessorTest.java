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

//    private String ep = "direct";
    private String ep = "seda";
    
    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() throws Exception {

                from("direct:startProcessorTest")
                	.log("start")
                	.to(ep + ":ProcessorTestProcessor")
                	.log("end")
                	.to(resultEndpoint);
                
                from(ep + ":ProcessorTestProcessor")
                	.log("seda in")
                	.process(new SimpleProcessor())
                	.log("seda out");
            }
        };
    }

    @Test
    public void test_message_goes_from_in_to_out() throws InterruptedException {
        String msgIn = "Camel";
        String msgExpected = "seda".equals(ep) ? "Camel" : "Hello Camel";

        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody(msgIn);
        resultEndpoint.assertIsSatisfied();
    }

}
