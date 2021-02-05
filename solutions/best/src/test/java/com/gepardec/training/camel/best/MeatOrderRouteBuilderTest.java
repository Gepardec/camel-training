package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.domain.OrderItem;
import com.gepardec.training.camel.commons.domain.OrderToProducer;
import com.gepardec.training.camel.commons.test.routetest.CamelRouteCDITest;
import com.gepardec.training.camel.commons.test.routetest.MockableEndpoint;
import com.gepardec.training.camel.commons.test.routetest.RouteId;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.Uri;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.cdi.Beans;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(CamelCdiRunner.class)
@Beans(classes = MeatOrderRouteBuilder.class)
public class MeatOrderRouteBuilderTest extends CamelRouteCDITest {

    @Inject
    @Uri("mock:result")
    @MockableEndpoint(MeatOrderRouteBuilder.OUTPUT_FILE_ENDPOINT_URI)
    @RouteId(MeatOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
    private MockEndpoint result;

    @Test
    public void testAmountUnder100_nothingInEndopoint(@Uri(MeatOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI) ProducerTemplate producer) throws InterruptedException {
        OrderToProducer orderToProducer = new OrderToProducer(new OrderItem(OrderItem.MEAT, 80), 42);

        result.expectedMessageCount(0);
        producer.sendBody(orderToProducer);
        result.assertIsSatisfied();
    }

    @Test
    public void testAmountAbove100_EndopointContainsCorrectMessage(@Uri(MeatOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI) ProducerTemplate producer) throws InterruptedException {
        OrderToProducer orderToProducer = new OrderToProducer(new OrderItem(OrderItem.MEAT, 120), 42);

        result.expectedMessageCount(1);
        producer.sendBody(orderToProducer);
        result.assertIsSatisfied();

        assertThat(result.getExchanges()).hasSize(1);
        String content = result.getExchanges().get(0).getIn().getBody(String.class);
        assertThat(content).isEqualToIgnoringWhitespace("parentId=42, amount=120, code=4");
    }
}
