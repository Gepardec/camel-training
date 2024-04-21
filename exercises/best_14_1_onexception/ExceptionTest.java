package com.gepardec.training.camel.best;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ExceptionTest extends CamelQuarkusTestSupport {

	private static final String TEST_NAME = "ExceptionTest";
	
    @Produce("direct:start" + TEST_NAME)
    private ProducerTemplate startProducer;

    @EndpointInject("mock:result" + TEST_NAME)
    private MockEndpoint resultEndpoint;

    @EndpointInject("mock:exception" + TEST_NAME)
    private MockEndpoint validationErrors;

    
    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() throws Exception {
            	
                from("direct:start" + TEST_NAME + "1")
            		.validate(bodyAs(String.class).regex("Hello \\d you!"))
            		.to(resultEndpoint);

                
				from("direct:start" + TEST_NAME + "3")
					.throwException(NullPointerException.class, "Intentional exception for testing purpose")
					.to(resultEndpoint);

                
            }
        };
    }

    @Test
    public void test_validate_body_ok() throws InterruptedException {
        String msgIn = "Hello 2 you!";
        String msgExpected = msgIn;

        resultEndpoint.expectedBodiesReceived(msgExpected);
        validationErrors.expectedMessageCount(0);

        startProducer.sendBody("direct:start" + TEST_NAME + "1", msgIn);
        resultEndpoint.assertIsSatisfied();
        validationErrors.assertIsSatisfied();
    }


    @Test
    public void test_validate_body_failed() throws InterruptedException {
        String msgIn = "Hello to you!";
        String msgExpected = msgIn;

        validationErrors.expectedBodiesReceived(msgExpected);
        resultEndpoint.expectedMessageCount(0);

        startProducer.sendBody("direct:start" + TEST_NAME + "1", msgIn);
        resultEndpoint.assertIsSatisfied();
        validationErrors.assertIsSatisfied();
    }

    @Test
    public void test_catch_only_validation_errors_thrown() throws InterruptedException {
        String msgIn = "Hello to you!";
        String msgExpected = msgIn;

        resultEndpoint.expectedMessageCount(0);
        validationErrors.expectedMessageCount(0);

        boolean exceptionThrown = false;
        try {
            startProducer.sendBody("direct:start" + TEST_NAME + "3", msgIn);            			
		} catch (CamelExecutionException e) {
			exceptionThrown = true;
		}
        assertTrue(exceptionThrown, "We expect a NullPointerException");
        resultEndpoint.assertIsSatisfied();
        validationErrors.assertIsSatisfied();
    }

}
