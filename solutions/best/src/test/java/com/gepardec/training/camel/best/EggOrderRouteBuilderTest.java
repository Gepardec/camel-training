package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.misc.FileUtils;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@QuarkusTest
@TestInstance(Lifecycle.PER_CLASS)
@TestProfile(EggOrderRouteBuilderTest.class)
public class EggOrderRouteBuilderTest extends CamelQuarkusTestSupport {

    private static final String ORDER_FILE = "json/order_eggs.json";
    private static final String EGG_ORDER_ITEM = "xml/egg_order_item.xml";

    @Inject
    CamelContext context;

    @EndpointInject("mock:resultEggOrderRouteBuilderTest")
    private MockEndpoint resultEndpoint;

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() throws Exception {

                from("direct:EggOrderRouteBuilderTestRest")
                        .log("Test message to rest EP: ${body}")
                        .to("direct:EggOrderRouteBuilderTestFrom");

                from("direct:EggOrderRouteBuilderTestDirectResult")
                        .convertBodyTo(String.class)
                        .log("Result EggOrderRouteBuilder.OUTPUT_JMS_ENDPOINT_URI: ${body}")
                        .to(resultEndpoint);
            }
        };
    }

    @BeforeAll
    public void beforeAll() throws Exception {

        AdviceWith.adviceWith(context, EntryRouteBuilder.ENTRY_REST_ENDOINT_ROUTE_ID,
                a -> a.replaceFromWith("direct:EggOrderRouteBuilderTestFrom"));

        AdviceWith.adviceWith(EggOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() {
                weaveByToUri(EggOrderRouteBuilder.OUTPUT_JMS_ENDPOINT_URI)
                        .replace()
                        .to("direct:EggOrderRouteBuilderTestDirectResult");
            }
        });

    }

    @Test
    public void test_rest_egg_order() throws Exception {

        String msgIn = FileUtils.getFileAsString(ORDER_FILE);
        String msgExpected = FileUtils.getFileAsString(EGG_ORDER_ITEM) + '\n';

        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(msgExpected);

        template()
                .sendBody("direct:EggOrderRouteBuilderTestRest", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

}
