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

    @Test
    public void inputOf4OrderItems_OutputWith4Messages() throws Exception {

    }

}