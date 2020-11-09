package com.gepardec.training.camel.best;


import com.gepardec.training.camel.commons.domain.Order;
import com.gepardec.training.camel.commons.domain.OrderToProducer;

import java.util.ArrayList;
import java.util.List;

public class OrderSplitter {

    public static List<OrderToProducer> splitOrder(Order order){
        List<OrderToProducer> result = new ArrayList<>();
        order.getItems().stream().forEach(item -> {
            OrderToProducer orderToProducer = new OrderToProducer(item, order.getPartnerId());
            result.add(orderToProducer);
        });
        return result;
    }
}
