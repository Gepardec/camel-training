package com.gepardec.trainings.camel.best;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;


public class SimpleTest extends CamelTestSupport {

	
    @Test
    public void test_simple_with_body() throws InterruptedException {
        String msgIn = "Gepardec";
        String msgExpected = "Willkommen bei Gepardec";

        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:result", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(msgExpected);
        
        
        template.sendBody("direct:start", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("direct:start")
                .log("Got message: ${body}")
                .setBody(simple("Willkommen bei ${body}"))
               .to("mock:result");
            }
        };
    } 
}