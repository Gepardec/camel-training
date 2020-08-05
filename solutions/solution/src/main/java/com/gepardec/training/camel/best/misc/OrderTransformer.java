package com.gepardec.training.camel.best.misc;

import com.gepardec.training.camel.best.config.ExchangeHeaders;
import com.gepardec.training.camel.best.domain.OrderItem;
import com.gepardec.training.camel.best.domain.OrderToProducer;
import org.apache.camel.Exchange;

public class OrderTransformer {
    public void transform (Exchange exchange){
        OrderItem item = exchange.getIn().getBody(OrderItem.class);

        OrderToProducer orderToProducer = new OrderToProducer();
        orderToProducer.setPartnerId(exchange.getIn().getHeader(ExchangeHeaders.PARTNER_ID, Long.class));
        orderToProducer.setAmount(item.getAmount());
        orderToProducer.setCode(item.getCode());

        exchange.getIn().setBody(orderToProducer);
    }
}
