package com.gepardec.training.camel.best.domain;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private long partnerId;
    private List<OrderItem> items;

    public long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(long partnerId) {
        this.partnerId = partnerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
