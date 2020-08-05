package com.gepardec.training.camel.best.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OrderToProducer")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderToProducer extends OrderItem{
    private long partnerId;

    public long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(long partnerId) {
        this.partnerId = partnerId;
    }
}
