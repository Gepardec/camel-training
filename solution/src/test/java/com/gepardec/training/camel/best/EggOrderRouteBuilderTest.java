package com.gepardec.training.camel.best;

import com.gepardec.training.camel.best.config.Endpoints;
import com.gepardec.training.camel.best.domain.OrderItem;
import com.gepardec.training.camel.best.domain.OrderToProducer;
import com.gepardec.training.camel.commons.endpoint.CamelEndpoint;
import com.gepardec.training.camel.commons.test.routetest.CamelRouteTest;
import org.apache.camel.Exchange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EggOrderRouteBuilderTest extends CamelRouteTest {

    @RouteUnderTest
    @InjectMocks
    private EggOrderRouteBuilder eggOrderRouteBuilder;

    @MockedEndpoint
    CamelEndpoint jmsEndpoint = Endpoints.EGG_ORDER_JMS_ENDPOINT;

    @Test
    public void correctInput_correctMessageToQueue(){
        OrderToProducer orderToProducer = new OrderToProducer();
        orderToProducer.setCode(OrderItem.EGG);
        orderToProducer.setAmount(2);
        orderToProducer.setPartnerId(3);

        sendToEndpoint(Endpoints.EGG_ORDER_ENTRY_SEDA_ENDPOINT, orderToProducer);
        Exchange exchange = pollFromEndpoint(jmsEndpoint);
        assertThat(exchange).isNotNull();
        assertThat(exchange.getIn().getBody()).isNotNull();
        assertThat(exchange.getIn().getBody(String.class))
                .containsIgnoringCase("orderToProducer>")
                .containsIgnoringCase("amount>2</")
                .containsIgnoringCase("code>1</")
                .containsIgnoringCase("partnerId>3</");
    }

    @Test
    public void wrongInput_noMessageToQueue(){
        sendToEndpoint(Endpoints.EGG_ORDER_ENTRY_SEDA_ENDPOINT, "string");
        Exchange exchange = pollFromEndpoint(jmsEndpoint);
        assertThat(exchange).isNull();
    }


}