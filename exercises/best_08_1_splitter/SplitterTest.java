package com.gepardec.training.camel.best;

import io.quarkus.test.junit.QuarkusTest;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Test;

import com.gepardec.training.camel.commons.domain.Order;
import com.gepardec.training.camel.commons.files.ExampleFiles;

@QuarkusTest
public class SplitterTest extends CamelQuarkusTestSupport {

	private static final String TEST_NAME = "SplitterTest";
	
    @Produce("direct:start" + TEST_NAME)
    private ProducerTemplate startProducer;

    @EndpointInject("mock:result" + TEST_NAME)
    private MockEndpoint resultEndpoint;

    @EndpointInject("mock:sum" + TEST_NAME)
    private MockEndpoint sumEndpoint;

    
    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() throws Exception {
                
                from("direct:start" + TEST_NAME + "5")
            		.unmarshal().json(Order.class)
            // TODO: Insert Splitter that splits Order into OrderToProducer items.
        				.log("Split off ${body}")
        				.to(resultEndpoint);
                
            }
        };
    }


    @Test
    public void test_split_order_with_new() throws InterruptedException {
        String msgIn = ExampleFiles.ORDER_JSON;

        resultEndpoint.expectedMessageCount(4);

        startProducer.sendBody("direct:start" + TEST_NAME + "5", msgIn);
        
        resultEndpoint.assertIsSatisfied();
    }

}
