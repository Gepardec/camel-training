package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.domain.Order;
import com.gepardec.training.camel.commons.domain.OrderItem;
import com.gepardec.training.camel.commons.domain.OrderToProducer;
import com.gepardec.training.camel.commons.test.routetest.CamelRouteCDITest;
import com.gepardec.training.camel.commons.test.routetest.MockableEndpoint;
import com.gepardec.training.camel.commons.test.routetest.RouteId;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.Uri;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.cdi.Beans;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.assertj.core.api.Assertions;
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
public class SplitterRouteBuilderTest extends CamelRouteCDITest {

    @Inject
    @Uri("mock:choice")
    @RouteId(SplitterRouteBuilder.SPLITTER_FROM_ENDOINT_URI)
    @MockableEndpoint(SplitterRouteBuilder.CHOICE_FROM_ENDOINT_URI)
    private MockEndpoint toChoiceEndpoint;

    @Inject
    @Uri("mock:egg_result")
    @RouteId(SplitterRouteBuilder.CHOICE_FROM_ENDOINT_URI)
    @MockableEndpoint(EggOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
    private MockEndpoint eggResult;

    @Inject
    @Uri("mock:pasta_result")
    @RouteId(SplitterRouteBuilder.CHOICE_FROM_ENDOINT_URI)
    @MockableEndpoint(PastaOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
    private MockEndpoint pastaResult;

    @Inject
    @Uri("mock:meat_result")
    @RouteId(SplitterRouteBuilder.CHOICE_FROM_ENDOINT_URI)
    @MockableEndpoint(MeatOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
    private MockEndpoint meatResult;

    @Inject
    @Uri("mock:milk_result")
    @RouteId(SplitterRouteBuilder.CHOICE_FROM_ENDOINT_URI)
    @MockableEndpoint(MilkOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
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
        toChoiceEndpoint.reset();
    }

    @Test
    public void inputOf4OrderItems_OutputWith4Messages(@Uri(SplitterRouteBuilder.SPLITTER_FROM_ENDOINT_URI) ProducerTemplate producer) throws Exception {
        toChoiceEndpoint.expectedMessageCount(4);

        producer.sendBody(order);
        toChoiceEndpoint.assertIsSatisfied();

        Assertions.assertThat(toChoiceEndpoint.getExchanges()).extracting(ex -> ex.getIn().getBody()).allMatch(o -> o.getClass().isAssignableFrom(OrderToProducer.class));
    }

    @Test
    public void correctInput_CorrectOutputToEggs(@Uri(SplitterRouteBuilder.CHOICE_FROM_ENDOINT_URI) ProducerTemplate producer) throws Exception {
        eggResult.expectedMessageCount(1);

        producer.sendBody(new OrderToProducer(order.getItems().get(0), 1));
        eggResult.assertIsSatisfied();

        final Exchange exchange = eggResult.getExchanges().get(0);
        assertThat(exchange).isNotNull();

        OrderToProducer order = exchange.getIn().getBody(OrderToProducer.class);
        assertThat(order).isNotNull();
        assertThat(order.getAmount()).isEqualTo(110);
        assertThat(order.getPartnerId()).isEqualTo(1L);
    }

    @Test
    public void correctInput_CorrectOutputToPasta(@Uri(SplitterRouteBuilder.CHOICE_FROM_ENDOINT_URI) ProducerTemplate producer) throws Exception {

        pastaResult.expectedMessageCount(1);

        // Then
        producer.sendBody(new OrderToProducer(order.getItems().get(1), 2));
        pastaResult.assertIsSatisfied();
        final Exchange exchange = pastaResult.getExchanges().get(0);
        assertThat(exchange).isNotNull();

        OrderToProducer order = exchange.getIn().getBody(OrderToProducer.class);
        assertThat(order).isNotNull();
        assertThat(order.getAmount()).isEqualTo(120);
        assertThat(order.getPartnerId()).isEqualTo(2L);
    }

    @Test
    public void correctInput_CorrectOutputToMilk(@Uri(SplitterRouteBuilder.CHOICE_FROM_ENDOINT_URI) ProducerTemplate producer) throws InterruptedException {
        milkResult.expectedMessageCount(1);

        // Then
        producer.sendBody(new OrderToProducer(order.getItems().get(2), 3));
        milkResult.assertIsSatisfied();
        final Exchange exchange = milkResult.getExchanges().get(0);
        assertThat(exchange).isNotNull();

        OrderToProducer order = exchange.getIn().getBody(OrderToProducer.class);
        assertThat(order).isNotNull();
        assertThat(order.getAmount()).isEqualTo(130);
        assertThat(order.getPartnerId()).isEqualTo(3L);
    }

    @Test
    public void correctInput_CorrectOutputToMeat(@Uri(SplitterRouteBuilder.CHOICE_FROM_ENDOINT_URI) ProducerTemplate producer) throws InterruptedException {
        meatResult.expectedMessageCount(1);

        // Then
        producer.sendBody(new OrderToProducer(order.getItems().get(3), 4));
        meatResult.assertIsSatisfied();
        final Exchange exchange = meatResult.getExchanges().get(0);
        assertThat(exchange).isNotNull();

        OrderToProducer order = exchange.getIn().getBody(OrderToProducer.class);
        assertThat(order).isNotNull();
        assertThat(order.getAmount()).isEqualTo(140);
        assertThat(order.getPartnerId()).isEqualTo(4L);
    }
}