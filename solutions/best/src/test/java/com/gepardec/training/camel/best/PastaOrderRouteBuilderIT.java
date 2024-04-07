package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.domain.OrderToProducer;
import com.gepardec.training.camel.commons.misc.FileUtils;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class PastaOrderRouteBuilderIT extends CamelQuarkusTestSupport {

    private static final String PASTA_ORDER_FILE = "json/order_pasta.json";
    private static final String PASTA_ORDER_ITEM = "json/pasta_order_item.json";

    @EndpointInject("mock:resultPastaOrderRouteBuilderTest")
    private MockEndpoint result;

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() throws Exception {

                from("direct:TestRestEP")
                        .toV("sql://select count(*) from order_to_producer", null, "global:countBefore")
                        .log("Test message to rest EP: ${body} countBefore: ${variable.global:countBefore} countAfter: ${variable.global:countAfter}")
                        .to(EntryRouteBuilderIT.ENTRY_REST_ENDOINT_URI);

                from("direct:TestDirectEP")
                        .toV("sql://select count(*) from order_to_producer", null, "global:countBefore")
                        .log("Test message to direct EP: ${body} countBefore: ${variable.global:countBefore} countAfter: ${variable.global:countAfter}")
                        .unmarshal().json(OrderToProducer.class)
                        .to(PastaOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
                        .log("sql://select count(*) from order_to_producer where id=CAST(:#${exchangeProperty.BestDbID} AS uuid)?dataSource=#my_db")
                        .to("sql://select count(*) from order_to_producer where id=CAST(:#${exchangeProperty.BestDbID} AS uuid)?dataSource=#my_db")
                        .log("count: ${body}");
                //           	.to(result);

                from(PastaOrderRouteBuilder.OUTPUT_FILE_ENDPOINT_URI)
                        .toV("sql://select count(*) from order_to_producer", null, "global:countAfter")
                        .log("Received message: ${body} countBefore: ${variable.global:countBefore} countAfter: ${variable.global:countAfter}")
                        .choice()
                        .when(variable("global:countBefore").isNotEqualTo(variable("global:countAfter")))
                        .setBody(constant("OK"))
                        .otherwise()
                        .setBody(simple(
                                "Something wrong! Received message: ${body} countBefore: ${variable.global:countBefore} countAfter: ${variable.global:countAfter}"))
                        .end()
                        .to(result);
            }
        };
    }

    @Test
    public void test_seda_ep_sents_to_file() throws Exception {

        String msgIn = FileUtils.getFileAsString(PASTA_ORDER_ITEM);
        String msgExpected = "OK";

        result.expectedMessageCount(1);
        template().sendBody("direct:TestDirectEP", msgIn);
        result.assertIsSatisfied();

        assertEquals(1, result.getExchanges().size());
        String content = result.getExchanges().get(0).getIn().getBody(String.class);
        assertEquals(msgExpected, content);
    }

    @Test
    public void test_rest_pasta_order() throws Exception {

        String msgIn = FileUtils.getFileAsString(PASTA_ORDER_FILE);
        String msgExpected = "OK";

        assertNotNull(msgIn);
        assertNotNull(msgExpected);

        result.expectedMessageCount(1);
        result.expectedBodiesReceived(msgExpected);

        template().sendBody("direct:TestRestEP", msgIn);
        result.assertIsSatisfied();
    }

}
