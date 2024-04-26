package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.domain.OrderToProducer;
import com.gepardec.training.camel.commons.misc.FileUtils;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestInstance(Lifecycle.PER_CLASS)
@TestProfile(PastaOrderRouteBuilderTest.class)
public class PastaOrderRouteBuilderTest extends CamelQuarkusTestSupport {

    private static final String PASTA_ORDER_FILE = "json/order_pasta.json";
    private static final String PASTA_ORDER_ITEM = "json/pasta_order_item.json";

    @Inject
    CamelContext context;

    @EndpointInject("mock:resultPastaAdviceRouteBuilderTest")
    private MockEndpoint result;

    @EndpointInject("direct://TestWeaveEP")
    private Endpoint weaveEP;

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() throws Exception {

                from("direct:TestRestEP")
                        .to("direct://PastaOrderRouteBuilderTestFrom");

                from("direct:TestDirectEP")
                        .unmarshal().json(OrderToProducer.class)
                        .to(PastaOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI);

                from(weaveEP)
                        .log("weaveEP get message ${body}")
                        .marshal().json(true)
                        .to(result);
            }
        };
    }

    @BeforeAll
    public void beforeAll() throws Exception {

        AdviceWith.adviceWith(context, EntryRouteBuilder.ENTRY_REST_ENDOINT_ROUTE_ID,
                a -> a.replaceFromWith("direct://PastaOrderRouteBuilderTestFrom"));

        AdviceWith.adviceWith(PastaOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() {
                //weaveAddLast().to("direct://TestWeaveEP");
                weaveByToUri(PastaOrderRouteBuilder.OUTPUT_SQL_ENDPOINT_URI).replace().to(weaveEP);
            }
        });
    }

    @Test
    public void test_advice_seda_ep_sents_to_file() throws Exception {

        String msgIn = FileUtils.getFileAsString(PASTA_ORDER_ITEM);
        String msgExpected = FileUtils.getFileAsString(PASTA_ORDER_ITEM);

        result.expectedMessageCount(1);
        result.expectedBodiesReceived(msgExpected);

        template().sendBody("direct:TestDirectEP", msgIn);
        System.out.println("!!!  assertIsSatisfied !!!");
        result.assertIsSatisfied();
    }

    @Test
    public void test_advice_rest_pasta_order() throws Exception {

        String msgIn = FileUtils.getFileAsString(PASTA_ORDER_FILE);
        String msgExpected = FileUtils.getFileAsString(PASTA_ORDER_ITEM);

        assertNotNull(msgIn);
        assertNotNull(msgExpected);

        result.expectedMessageCount(1);
        result.expectedBodiesReceived(msgExpected);

        template().sendBody("direct:TestRestEP", msgIn);
        result.assertIsSatisfied();

    }
}
