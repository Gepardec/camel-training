package com.gepardec.training.camel.best;

import java.util.ArrayList;
import java.util.List;

import com.gepardec.training.camel.commons.domain.Order;
import com.gepardec.training.camel.commons.domain.OrderItem;
import com.gepardec.training.camel.commons.domain.OrderToProducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

public class OrderSplitter {

    public static List<OrderToProducer> splitOrder(Order order) {
        List<OrderToProducer> result = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
			result.add(new OrderToProducer(item, order.getPartnerId()));
		}
        return result;
    }
}
