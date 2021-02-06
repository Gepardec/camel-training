package com.gepardec.trainings.camel.best;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SimpleSpringRouteTest extends CamelSpringTestSupport {

    @Override
    @Before
    public void setUp() throws Exception {
        deleteDirectory("target/messages");
        super.setUp();
    }
	
    @Test
    public void msg_sent_to_infile_is_processed() throws InterruptedException {
        String msgIn = "Das ist mein Body";
        String msgExpected = "Das ist mein Body";

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
              .to("direct:infile");
                
                
              from("file:target/messages/somewhere")
                .to("mock:result");
            }
        };
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
    }
}