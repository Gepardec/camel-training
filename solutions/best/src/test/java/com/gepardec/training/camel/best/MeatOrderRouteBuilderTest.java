package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.domain.OrderItem;
import com.gepardec.training.camel.commons.domain.OrderToProducer;
import com.gepardec.training.camel.commons.misc.FileUtils;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class MeatOrderRouteBuilderTest extends CamelQuarkusTestSupport {

    private static final String MEET_ORDER_FILE = "json/order_meat.json";

    @EndpointInject("mock:resultMeatOrderRouteBuilderTest")
    private MockEndpoint result;

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() throws Exception {

                from("direct:TestRestEP")
                        .log("Test message to rest EP: ${body}")
                        .to("rest://post:/best/?host=localhost:8081");

                from("direct:TestDirectEP")
                        .log("Test message to seda: ${body}")
                        .to(MeatOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI);

                from(MeatOrderRouteBuilder.OUTPUT_FILE_ENDPOINT_URI)
                        .log("Received message: ${body}")
                        .to(result);
            }
        };
    }

    @Test
    public void testAmountUnder100_nothingInEndopoint() throws InterruptedException {
        OrderToProducer orderToProducer = new OrderToProducer(new OrderItem(OrderItem.MEAT, 80), 42);

        result.expectedMessageCount(0);
        template().sendBody("direct:TestDirectEP", orderToProducer);
        result.assertIsSatisfied();
    }

    @Test
    public void testAmountAbove100_EndopointContainsCorrectMessage() throws InterruptedException {
        OrderToProducer msgIn = new OrderToProducer(new OrderItem(OrderItem.MEAT, 120), 42);
        String msgExpected = "parentId=42, amount=120, code=4";

        result.expectedMessageCount(1);
        result.expectedBodiesReceived(msgExpected);
        
        template().sendBody("direct:TestDirectEP", msgIn);
        result.assertIsSatisfied();
    }

    @Test
    public void test_rest_meat_order() throws Exception {

        String msgIn = FileUtils.getFileAsString(MEET_ORDER_FILE);
        String msgExpected = "parentId=3, amount=154, code=4";

        assertNotNull(msgIn);
        assertNotNull(msgExpected);

        result.expectedMessageCount(1);
        result.expectedBodiesReceived(msgExpected);

        template().sendBody("direct:TestRestEP", msgIn);
        result.assertIsSatisfied();
    }

}
