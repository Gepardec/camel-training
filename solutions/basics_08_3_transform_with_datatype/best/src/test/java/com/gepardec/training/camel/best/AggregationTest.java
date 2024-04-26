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
public class AggregationTest extends CamelQuarkusTestSupport {

	private static final String TEST_NAME = "AggregationTest";
	
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

                from("direct:start" + TEST_NAME + "1")
                	.log("get body: ${body}")
                	.aggregate(new StringAggregationStrategy())
                		.constant(true)
             //   		.completionSize(3)
                		.completionTimeout(500L)
                	.to(resultEndpoint);
                
                
            }
        };
    }

    @Test
    public void test_aggregate_strings() throws InterruptedException {

        String msgExpected = "apple+cherry+orange+pear";

        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody("direct:start" + TEST_NAME + "1", "apple");
        startProducer.sendBody("direct:start" + TEST_NAME + "1", "cherry");
        startProducer.sendBody("direct:start" + TEST_NAME + "1", "orange");
        startProducer.sendBody("direct:start" + TEST_NAME + "1", "pear");

        resultEndpoint.assertIsSatisfied();
    }


}
