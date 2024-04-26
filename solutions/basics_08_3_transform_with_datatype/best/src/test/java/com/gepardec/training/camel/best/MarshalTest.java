package com.gepardec.training.camel.best;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.glassfish.jaxb.runtime.v2.runtime.reflect.opt.Const;
import org.junit.jupiter.api.Test;

import com.gepardec.training.camel.commons.domain.Order;
import com.gepardec.training.camel.commons.domain.OrderToProducer;
import com.gepardec.training.camel.commons.files.ExampleFiles;

@QuarkusTest
public class MarshalTest extends CamelQuarkusTestSupport {

	private static final String TEST_NAME = "MarshalTest";
	
    @Produce("direct:start" + TEST_NAME)
    private ProducerTemplate startProducer;

    @EndpointInject("mock:result" + TEST_NAME)
    private MockEndpoint resultEndpoint;

    
    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() throws Exception {

            	
                from("direct:start" + TEST_NAME)
                	.log("start")
                	.unmarshal().json(OrderToProducer.class)
                	.process(new Processor() {
                		public void process(Exchange exchange) throws Exception {
                			exchange.getIn().getBody(OrderToProducer.class).setAmount(111);
                		};
                	})
                	.marshal().json()
                	.log("end")
                	.to(resultEndpoint);
                                
            }
        };
    }

    @Test
    public void test_marshall_with_JacksonDataFormat() throws InterruptedException {
        String msgIn = ExampleFiles.ORDER_TO_PRODUCER_EGGS_JSON;
        String msgExpected = "{\"code\":1,\"amount\":111,\"partnerId\":1}";

        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody(msgIn);
        resultEndpoint.assertIsSatisfied();
    }

}
