package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.domain.OrderItem;
import com.gepardec.training.camel.commons.domain.OrderToProducer;
import com.gepardec.training.camel.commons.test.routetest.CamelRouteCDITest;
import com.gepardec.training.camel.commons.test.routetest.RouteId;
import com.gepardec.training.camel.commons.test.routetest.MockableEndpoint;
import org.apache.camel.Exchange;
import org.apache.camel.cdi.Uri;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.cdi.Beans;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(CamelCdiRunner.class)
@Beans(classes = EggOrderRouteBuilder.class)
public class EggOrderRouteBuilderTest extends CamelRouteCDITest {

    @Inject
    @Uri("mock:result")
    @MockableEndpoint(id = EggOrderRouteBuilder.OUTPUT_JMS_ENDPOINT_ID)
    @RouteId(EggOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
    private MockEndpoint result;

    @Test
    public void correctInput_messageInQueue() throws InterruptedException {
        OrderToProducer orderToProducer = new OrderToProducer();
        orderToProducer.setCode(OrderItem.EGG);
        orderToProducer.setAmount(2);
        orderToProducer.setPartnerId(3);

        result.expectedMessageCount(1);
        
        // Then
        sendToEndpoint(EggOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI, orderToProducer);
        result.assertIsSatisfied();
        final Exchange exchange = result.getExchanges().get(0);
        assertThat(exchange).isNotNull();
        assertThat(exchange.getIn().getBody()).isNotNull();
        assertThat(exchange.getIn().getBody(String.class))
                .containsIgnoringCase("orderToProducer>")
                .containsIgnoringCase("amount>2</")
                .containsIgnoringCase("code>1</")
                .containsIgnoringCase("partnerId>3</");
        result.reset();
    }

    @Test
    public void wrongInput_noMessageToQueue() throws InterruptedException{
        result.expectedMessageCount(0);
        // Then
        sendToEndpoint(EggOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI, "string");
        result.assertIsSatisfied();
        result.reset();
    }

}