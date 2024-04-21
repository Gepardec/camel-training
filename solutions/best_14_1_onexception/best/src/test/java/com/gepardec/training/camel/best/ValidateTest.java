package com.gepardec.training.camel.best;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Test;

import com.gepardec.training.camel.commons.domain.ExampleOrders;
import com.gepardec.training.camel.commons.domain.OrderToProducer;
import com.gepardec.training.camel.commons.files.ExampleFiles;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ValidateTest extends CamelQuarkusTestSupport {

	private static final String TEST_NAME = "ValidateTest";
	
    @Produce("direct:start" + TEST_NAME)
    private ProducerTemplate startProducer;

    @EndpointInject("mock:result" + TEST_NAME)
    private MockEndpoint resultEndpoint;

    
    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() throws Exception {
            	
				
                from("direct:start" + TEST_NAME + "1")
                	.setHeader("bar", constant(110))
                	.validate(header("bar").isGreaterThan(100))
                	.to(resultEndpoint);
                
                from("direct:start" + TEST_NAME + "2")
            		.validate(bodyAs(String.class).regex("Hello \\d you!"))
            		.to(resultEndpoint);
                            
                from("direct:start" + TEST_NAME + "3")
                .doTry()
                	.to("direct:validateRegex")
            		.setBody(constant("OK"))
            	.doCatch(ValidationException.class)
            		.setBody(constant("Failed"))   
            	.end()
                .to(resultEndpoint);
                        
                // Validator routes
            	validator()
        			.type("myjava:order2producer")
        			.withJava(Order2ProducerValidator.class);


                from("direct:start" + TEST_NAME + "4")
            	.doTry()
            		.to("direct:validate")
            		.setBody(constant("OK"))                		
            	.doCatch(ValidationException.class)
            		.setBody(constant("Failed"))   
            	.end()
        		.to(resultEndpoint);
                 
                from("direct:start" + TEST_NAME + "5")
            	.doTry()
            		.to("direct:validate")
            		.setBody(constant("OK"))                		
            	.doCatch(ValidationException.class)
            		.setBody(constant("Failed"))   
            	.end()
        		.to(resultEndpoint);
                 
                from("direct:validate")
                	.inputTypeWithValidate("myjava:order2producer")
                	.to("log:inValidation");
                
                from("direct:validateRegex")
        			.validate(bodyAs(String.class).regex("Hello \\d you!"));


            }
        };
    }

    @Test
    public void test_validate_header() throws InterruptedException {
        String msgIn = "Hello for you!";
        String msgExpected = msgIn;

        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody("direct:start" + TEST_NAME + "1", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void test_validate_body_regex() throws InterruptedException {
        String msgIn = "Hello 2 you!";
        String msgExpected = msgIn;

        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody("direct:start" + TEST_NAME + "2", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void test_validate_body_regex_failed() throws InterruptedException {
        String msgIn = "Hello for you!";
        String msgExpected = "Failed";

        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody("direct:start" + TEST_NAME + "3", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void test_validator_for_order_failed() throws InterruptedException {
        String msgIn = ExampleFiles.ORDER_TO_PRODUCER_EGGS_JSON;
        String msgExpected = "Failed";

        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody("direct:start" + TEST_NAME + "4", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void test_validator_for_order_ok() throws InterruptedException {
        OrderToProducer msgIn = ExampleOrders.ORDER_TO_PRODUCER_EGGS;
        String msgExpected = "OK";

        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody("direct:start" + TEST_NAME + "5", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

}
