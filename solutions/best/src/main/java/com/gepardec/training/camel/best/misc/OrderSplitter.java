package com.gepardec.training.camel.best.misc;

import com.gepardec.training.camel.best.config.ExchangeHeaders;
import com.gepardec.training.camel.best.domain.Order;
import com.gepardec.training.camel.best.domain.OrderItem;
import com.gepardec.training.camel.best.domain.OrderToProducer;
import com.sun.org.apache.xpath.internal.operations.Or;

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
