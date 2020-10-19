package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class ShoppingListRouteBuilder extends RouteBuilder {

    public static final String ENTRY_SEDA_ENDOINT_URI = "seda:best_shoppingList_entry";
    public static final String ENTRY_SEDA_ENDOINT_ID = "best_shoppingList_entry";

    public static final String ROUTE_ID = "ShoppingListRouteBuilder";

    @Override
    public void configure() {
        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .handled(true);

        from(ENTRY_SEDA_ENDOINT_URI).id(ENTRY_SEDA_ENDOINT_ID)
                .routeId(ROUTE_ID)
                .marshal().json()
                .to("file:src/test/resources/logs?filename=logfile.txt&fileExist=Append");
    }
}
