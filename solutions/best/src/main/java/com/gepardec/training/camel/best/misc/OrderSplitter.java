package com.gepardec.training.camel.best.misc;

import com.gepardec.training.camel.best.domain.Order;
import com.gepardec.training.camel.best.domain.OrderItem;

import java.util.List;

public class OrderSplitter {

    public static List<OrderItem> splitOrder(Order order){
        return order.getItems();
    }
}
