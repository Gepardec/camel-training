package com.gepardec.training.camel.best;

import com.gepardec.training.camel.best.domain.ShoppingList;
import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class ShoppingListRouteBuilder extends RouteBuilder {

    public static final String ENTRY_SEDA_ENDOINT_URI = "seda:best_shoppingList_entry";
    public static final String ENTRY_SEDA_ENDOINT_ID = "best_shoppingList_entry";

    public static final String ENTRY_SEDA_ENDOINT_URI_FROM_JSON = "seda:best_shoppingList_json_entry";
    public static final String ENTRY_SEDA_ENDOINT_ID_FROM_JSON = "best_shoppingList_json_entry";

    public static final String ROUTE_ID = "ShoppingListRouteBuilder";
    public static final String ROUTE_ID_FROM_JSON = "ShoppingListFromJsonRouteBuilder";


    @Override
    public void configure() {
        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .handled(true);

        from(ENTRY_SEDA_ENDOINT_URI).id(ENTRY_SEDA_ENDOINT_ID)
                .routeId(ROUTE_ID)
                .marshal().json()
                //file will be overwritten,can be avoided with "fileExists=append"
                .to("file:src/test/resources/logs?filename=logfile.txt");


        from("direct:triggerFileRoute")
                .routeId("triggerFileRoute")
                .log("test")
                .to("file:src/test/resources/logs?filename=logfile.txt");

        from("file:src/test/resources/logs?filename=logfile.txt")
                .routeId(ROUTE_ID_FROM_JSON)
                .autoStartup(false)
                .unmarshal(new JacksonDataFormat(ShoppingList.class))
                .process(exchange -> System.out.println())
                .to(ENTRY_SEDA_ENDOINT_URI_FROM_JSON).id(ENTRY_SEDA_ENDOINT_ID_FROM_JSON);

//        from("file-watch:src/test/resources/logs")
//                .log("File event: ${header.CamelFileEventType} occurred on file ${header.CamelFileName} at ${header.CamelFileLastModified}");
    }
}
