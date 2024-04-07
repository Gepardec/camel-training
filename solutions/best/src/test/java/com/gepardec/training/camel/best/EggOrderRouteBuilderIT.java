package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.domain.Order;
import com.gepardec.training.camel.commons.misc.FileUtils;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class EggOrderRouteBuilderIT extends CamelQuarkusTestSupport {

    private static final String ORDER_FILE = "json/order_eggs.json";
    private static final String ORDER_EXPECTED = "json/first_order_item.json";
    private static final String EGG_ORDER_ITEM = "xml/egg_order_item.xml";

    @EndpointInject("mock:resultEntryRouteBuilderTest")
    private MockEndpoint resultEndpoint;

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() throws Exception {

                from("direct:EntryRouteBuilderTestDirect")
                        .log("Test message: ${body}")
                        .unmarshal().json(Order.class)
                        .to(EntryRouteBuilder.ENTRY_DIRECT_ENDOINT_URI);

                from(EntryRouteBuilder.ORDER_ENDOINT_URI)
                        .log("Received message: ${body}")
                        .to(resultEndpoint);

                from("direct:EntryRouteBuilderTestRest")
                        .log("Test message to rest EP: ${body}")
                        .to(EntryRouteBuilderIT.ENTRY_REST_ENDOINT_URI);

                from(EggOrderRouteBuilder.OUTPUT_JMS_ENDPOINT_URI)
                        .convertBodyTo(String.class)
                        .log("Result EggOrderRouteBuilder.OUTPUT_JMS_ENDPOINT_URI: ${body}")
                        .to(resultEndpoint);
            }
        };
    }

    @Test
    public void test_direct_egg_order() throws Exception {
        String msgIn = FileUtils.getFileAsString(ORDER_FILE);
        String msgExpected = FileUtils.getFileAsString(EGG_ORDER_ITEM) + '\n';

        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(msgExpected);

        template().sendBody("direct:EntryRouteBuilderTestDirect", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void test_rest_egg_order() throws Exception {

        String msgIn = FileUtils.getFileAsString(ORDER_FILE);
        String msgExpected = FileUtils.getFileAsString(EGG_ORDER_ITEM) + '\n';

        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(msgExpected);

        template()
                .sendBody("direct:EntryRouteBuilderTestRest", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

}
