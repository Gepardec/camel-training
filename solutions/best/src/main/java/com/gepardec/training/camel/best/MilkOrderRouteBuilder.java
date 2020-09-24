package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class MilkOrderRouteBuilder extends RouteBuilder {

    public static final String ENTRY_SEDA_ENDOINT_URI = "seda:milk_order_entry";
    public static final String ENTRY_SEDA_ENDOINT_ID = "milk_order_entry";

    public static final String ROUTE_ID = "MilkOrderRouteBuilder";

    @Override
    public void configure() {

        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .handled(true);

        from(ENTRY_SEDA_ENDOINT_URI)
                .routeId(ROUTE_ID)
                .log("MILK ${body}");
    }
}
