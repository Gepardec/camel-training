package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.domain.OrderToProducer;
import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class MeatOrderRouteBuilder extends RouteBuilder {

    public static final String ENTRY_SEDA_ENDOINT_URI = "seda://meat_order_entry";

    @Override
    public void configure() {

        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .handled(true);

        from(ENTRY_SEDA_ENDOINT_URI)
                .routeId(ENTRY_SEDA_ENDOINT_URI)
                .log("MEAT ${body}");
    }
}
