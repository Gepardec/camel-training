package com.gepardec.training.camel.commons.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OrderToProducer")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderToProducer extends OrderItem{

    private long partnerId;

    public OrderToProducer(OrderItem item, long partnerId) {
        this.setCode(item.getCode());
        this.setAmount(item.getAmount());
        this.setPartnerId(partnerId);
    }

    public OrderToProducer() {
        super();
    }

    public long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(long partnerId) {
        this.partnerId = partnerId;
    }
}
