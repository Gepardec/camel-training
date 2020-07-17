package com.gepardec.training.camel.best;

import com.gepardec.training.camel.best.config.Endpoints;
import com.gepardec.training.camel.best.domain.Order;
import com.gepardec.training.camel.best.domain.OrderItem;
import com.gepardec.training.camel.best.domain.OrderToProducer;
import com.gepardec.training.camel.commons.endpoint.CamelEndpoint;
import com.gepardec.training.camel.commons.test.routetest.CamelRouteTest;
import org.apache.camel.Exchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SplitterRouteBuilderTest extends CamelRouteTest {

    @RouteUnderTest
    @InjectMocks
    private SplitterRouteBuilder splitterRouteBuilder;

    @MockedEndpoint
    private CamelEndpoint eggsMockedEndpoint = Endpoints.EGG_ORDER_ENTRY_SEDA_ENDPOINT;

    @MockedEndpoint
    private CamelEndpoint meatMockedEndpoint = Endpoints.MEAT_ORDER_ENTRY_SEDA_ENDPOINT;

    @MockedEndpoint
    private CamelEndpoint milkMockedEndpoint = Endpoints.MILK_ORDER_ENTRY_SEDA_ENDPOINT;

    @MockedEndpoint
    private CamelEndpoint pastaMockedEndpoint = Endpoints.PASTA_ORDER_ENTRY_SEDA_ENDPOINT;

    private Order order;

    @BeforeEach
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

    @Test
    public void correctInput_CorrectOutputToEggs() throws IOException {
        sendToEndpoint(Endpoints.SPLITTER_ENTRY_SEDA_ENDPOINT, order);
        Exchange exchange = pollFromEndpoint(eggsMockedEndpoint);
        assertThat(exchange).isNotNull();

        OrderToProducer order = exchange.getIn().getBody(OrderToProducer.class);
        assertThat(order).isNotNull();
        assertThat(order.getAmount()).isEqualTo(110);
        assertThat(order.getPartnerId()).isEqualTo(1L);
    }

    @Test
    public void correctInput_CorrectOutputToPasta() throws IOException {
        sendToEndpoint(Endpoints.SPLITTER_ENTRY_SEDA_ENDPOINT, order);
        Exchange exchange = pollFromEndpoint(pastaMockedEndpoint);
        assertThat(exchange).isNotNull();

        OrderToProducer order = exchange.getIn().getBody(OrderToProducer.class);
        assertThat(order).isNotNull();
        assertThat(order.getAmount()).isEqualTo(120);
        assertThat(order.getPartnerId()).isEqualTo(1L);
    }

    @Test
    public void correctInput_CorrectOutputToMilk() throws IOException {
        sendToEndpoint(Endpoints.SPLITTER_ENTRY_SEDA_ENDPOINT, order);
        Exchange exchange = pollFromEndpoint(milkMockedEndpoint);
        assertThat(exchange).isNotNull();

        OrderToProducer order = exchange.getIn().getBody(OrderToProducer.class);
        assertThat(order).isNotNull();
        assertThat(order.getAmount()).isEqualTo(130);
        assertThat(order.getPartnerId()).isEqualTo(1L);
    }

    @Test
    public void correctInput_CorrectOutputToMeat() throws IOException {
        sendToEndpoint(Endpoints.SPLITTER_ENTRY_SEDA_ENDPOINT, order);
        Exchange exchange = pollFromEndpoint(meatMockedEndpoint);
        assertThat(exchange).isNotNull();

        OrderToProducer order = exchange.getIn().getBody(OrderToProducer.class);
        assertThat(order).isNotNull();
        assertThat(order.getAmount()).isEqualTo(140);
        assertThat(order.getPartnerId()).isEqualTo(1L);
    }

}