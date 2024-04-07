package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.domain.OrderToProducer;
import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.camel.builder.RouteBuilder;

public final class MeatOrderRouteBuilder extends RouteBuilder {

    public static final String ENTRY_SEDA_ENDOINT_URI = "seda://meat_order_entry";
    public static final String OUTPUT_FILE_ENDPOINT_URI = "file://tmp.file";

    @Override
    public void configure() {

        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .handled(true);

        from(ENTRY_SEDA_ENDOINT_URI)
                .routeId(ENTRY_SEDA_ENDOINT_URI)
                .validate(exchange -> exchange.getIn().getBody(OrderToProducer.class).getAmount() >= 100)
                .process(exchange -> {
                    OrderToProducer orderToProducer = exchange.getIn().getBody(OrderToProducer.class);
                    StringBuilder builder = new StringBuilder();
                    builder.append("parentId=").append(orderToProducer.getPartnerId())
                            .append(", amount=").append(orderToProducer.getAmount())
                            .append(", code=").append(orderToProducer.getCode());
                    exchange.getIn().setBody(builder.toString());
                })
                .to(OUTPUT_FILE_ENDPOINT_URI);
    }
}
