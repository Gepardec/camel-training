package com.gepardec.training.camel.best;

import com.gepardec.training.camel.best.domain.Order;
import com.gepardec.training.camel.best.domain.OrderItem;
import com.gepardec.training.camel.best.domain.OrderToProducer;
import com.gepardec.training.camel.commons.test.routetest.CamelRouteCDITest;
import com.gepardec.training.camel.commons.test.routetest.MockedEndpointId;
import com.gepardec.training.camel.commons.test.routetest.MockedRouteId;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.Uri;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.cdi.Beans;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(CamelCdiRunner.class)
@Beans(classes = SplitterRouteBuilder.class)
@MockedRouteId(SplitterRouteBuilder.ROUTE_ID)
public class SplitterRouteBuilderTest extends CamelRouteCDITest {

    @Inject
    @Uri("mock:egg_result")
    @MockedEndpointId(EggOrderRouteBuilder.ENTRY_SEDA_ENDOINT_ID)
    private MockEndpoint eggResult;

    @Inject
    @Uri("mock:pasta_result")
    @MockedEndpointId(PastaOrderRouteBuilder.ENTRY_SEDA_ENDOINT_ID)
    private MockEndpoint pastaResult;

    @Inject
    @Uri("mock:meat_result")
    @MockedEndpointId(MeatOrderRouteBuilder.ENTRY_SEDA_ENDOINT_ID)
    private MockEndpoint meatResult;

    @Inject
    @Uri("mock:milk_result")
    @MockedEndpointId(MilkOrderRouteBuilder.ENTRY_SEDA_ENDOINT_ID)
    private MockEndpoint milkResult;

    private Order order;

    @Before
    public void setup() {
        order = new Order();
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(OrderItem.EGG, 110));
        items.add(new OrderItem(OrderItem.PASTA, 120));
        items.add(new OrderItem(OrderItem.MILK, 130));
        items.add(new OrderItem(OrderItem.MEAT, 140));

        order.setItems(items);
        order.setPartnerId(1L);

    }

    @After
    public void reset(){
        eggResult.reset();
        pastaResult.reset();
        meatResult.reset();
        milkResult.reset();
    }

    @Test
    public void correctInput_CorrectOutputToEggs(@Uri(SplitterRouteBuilder.ENTRY_SEDA_ENDOINT_URI) ProducerTemplate producer) throws Exception {
        eggResult.expectedMessageCount(1);

        // Then
        //sendToEndpoint(SplitterRouteBuilder.ENTRY_SEDA_ENDOINT_URI, order);
        producer.sendBody(order);
        eggResult.assertIsSatisfied();

        final Exchange exchange = eggResult.getExchanges().get(0);
        assertThat(exchange).isNotNull();

        OrderToProducer order = exchange.getIn().getBody(OrderToProducer.class);
        assertThat(order).isNotNull();
        assertThat(order.getAmount()).isEqualTo(110);
        assertThat(order.getPartnerId()).isEqualTo(1L);
    }

    @Test
    public void correctInput_CorrectOutputToPasta(@Uri(SplitterRouteBuilder.ENTRY_SEDA_ENDOINT_URI) ProducerTemplate producer) throws Exception {

        pastaResult.expectedMessageCount(1);

        // Then
        producer.sendBody(order);
        pastaResult.assertIsSatisfied();
        final Exchange exchange = pastaResult.getExchanges().get(0);
        assertThat(exchange).isNotNull();

        OrderToProducer order = exchange.getIn().getBody(OrderToProducer.class);
        assertThat(order).isNotNull();
        assertThat(order.getAmount()).isEqualTo(120);
        assertThat(order.getPartnerId()).isEqualTo(1L);
    }

    @Test
    public void correctInput_CorrectOutputToMilk(@Uri(SplitterRouteBuilder.ENTRY_SEDA_ENDOINT_URI) ProducerTemplate producer) throws InterruptedException {
        milkResult.expectedMessageCount(1);

        // Then
        producer.sendBody(order);
        milkResult.assertIsSatisfied();
        final Exchange exchange = milkResult.getExchanges().get(0);
        assertThat(exchange).isNotNull();

        OrderToProducer order = exchange.getIn().getBody(OrderToProducer.class);
        assertThat(order).isNotNull();
        assertThat(order.getAmount()).isEqualTo(130);
        assertThat(order.getPartnerId()).isEqualTo(1L);
    }

    @Test
    public void correctInput_CorrectOutputToMeat(@Uri(SplitterRouteBuilder.ENTRY_SEDA_ENDOINT_URI) ProducerTemplate producer) throws InterruptedException {
        meatResult.expectedMessageCount(1);

        // Then
        producer.sendBody(order);
        meatResult.assertIsSatisfied();
        final Exchange exchange = meatResult.getExchanges().get(0);
        assertThat(exchange).isNotNull();

        OrderToProducer order = exchange.getIn().getBody(OrderToProducer.class);
        assertThat(order).isNotNull();
        assertThat(order.getAmount()).isEqualTo(140);
        assertThat(order.getPartnerId()).isEqualTo(1L);
    }

}