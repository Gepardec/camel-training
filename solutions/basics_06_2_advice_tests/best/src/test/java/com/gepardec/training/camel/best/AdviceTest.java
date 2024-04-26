package com.gepardec.training.camel.best;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestInstance(Lifecycle.PER_CLASS)
@TestProfile(AdviceTest.class)
public class AdviceTest extends CamelQuarkusTestSupport {

	private static final String TEST_NAME = "AdviceTest";
	
    @Produce("direct:start" + TEST_NAME)
    private ProducerTemplate startProducer;

    @EndpointInject("mock:result" + TEST_NAME)
    private MockEndpoint resultEndpoint;

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() throws Exception {

				
                from("direct:start" + TEST_NAME + "1")
                	.log("Got message: ${body}")
                	.to("direct:FileRouteTestFrom");
                
                from("direct:FileRouteTestTo")
                	.to(resultEndpoint);
                
            }
        };
    }

    @BeforeAll
    public void beforeAll() throws Exception {

        AdviceWith.adviceWith(context, "FileRouteId",
                a -> a.replaceFromWith("direct:FileRouteTestFrom"));

        AdviceWith.adviceWith("FileRouteId", context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() {
                weaveByToUri("file:target/messages/out")
                        .replace()
                        .to("direct:FileRouteTestTo");
            }
        });

    }

    @Test
    public void test_validate_header() throws InterruptedException {
        String msgIn = "Camel";
        String msgExpected = "Hello Camel!";

        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody("direct:start" + TEST_NAME + "1", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

}
