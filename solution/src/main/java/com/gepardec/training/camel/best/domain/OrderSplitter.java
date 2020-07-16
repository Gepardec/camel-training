package com.gepardec.training.camel.best.domain;

import java.util.List;

public class OrderSplitter {

    public static List<OrderItem> splitOrder(Order order){
        return order.getItems();
    }
}
