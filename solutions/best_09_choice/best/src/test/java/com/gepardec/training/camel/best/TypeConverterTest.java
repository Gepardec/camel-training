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
import com.gepardec.training.camel.commons.domain.OrderItem;
import com.gepardec.training.camel.commons.domain.OrderToProducer;
import com.gepardec.training.camel.commons.dto.OrderItemDto;
import com.gepardec.training.camel.commons.files.ExampleFiles;

@QuarkusTest
public class TypeConverterTest extends CamelQuarkusTestSupport {

	private static final String TEST_NAME = "TypeConverterTest";
	
    @Produce("direct:start" + TEST_NAME)
    private ProducerTemplate startProducer;

    @EndpointInject("mock:result" + TEST_NAME)
    private MockEndpoint resultEndpoint;

    
    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() throws Exception {

            	// TODO: register type converters

                from("direct:start" + TEST_NAME + "0")
            		.to(resultEndpoint);
                            
                from("direct:start" + TEST_NAME + "1")
            		.setBody(simple("Body is ${body.class}")) // OrderItem
            		.to(resultEndpoint);
                            
                from("direct:start" + TEST_NAME + "2")
                	.convertBodyTo(OrderItemDto.class)
            		.setBody(simple("Body is ${body.class}")) // OrderItemDto
            		.to(resultEndpoint);
                
                
                from("direct:start" + TEST_NAME + "7") // get OrderItemDto 
                	.inputType(OrderItem.class)
                	.convertBodyTo(OrderItemDto.class)
                	.setBody(simple("Body is ${body.class}")) // OrderItemDto
                	.to(resultEndpoint);
            }
        };
    }

    @Test
    public void test_implicit_use_of_type_convertet_in_mock_endpoint() throws InterruptedException {
        OrderItem msgIn = new OrderItem(OrderItem.MILK, 5);
        OrderItemDto msgExpected = new OrderItemDto(OrderItem.MILK, 5);

        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody("direct:start" + TEST_NAME + "0", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void test_input_type_is_OrderItem() throws InterruptedException {
        OrderItem msgIn = new OrderItem(OrderItem.MILK, 5);
        String msgExpected = "Body is class com.gepardec.training.camel.commons.domain.OrderItem";

        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody("direct:start" + TEST_NAME + "1", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void test_explicit_conversion_convertBodyTo() throws InterruptedException {
        OrderItem msgIn = new OrderItem(OrderItem.MILK, 5);
        String msgExpected = "Body is class com.gepardec.training.camel.commons.dto.OrderItemDto";

        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody("direct:start" + TEST_NAME + "2", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void test_toDto_and_reverse() throws InterruptedException {
    	OrderItemDto msgIn = new OrderItemDto(OrderItem.MILK, 5);
        String msgExpected = "Body is class com.gepardec.training.camel.commons.dto.OrderItemDto";

        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody("direct:start" + TEST_NAME + "7", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

}
