package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.domain.ShoppingList;
import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;

public class ShoppingListRouteBuilder extends RouteBuilder {

    private static final String FILE_LOGFILE_ENDPOINT = "file://target/logs?filename=logfile.txt";

    public static final String ENTRY_SEDA_ENDOINT_URI = "direct://best_shoppingList_entry";

    public static final String ENTRY_SEDA_ENDOINT_URI_FROM_JSON = "seda://best_shoppingList_json_entry";
    public static final String ENTRY_SEDA_ENDOINT_ID_FROM_JSON = "best_shoppingList_json_entry";

    public static final String ROUTE_ID = "ShoppingListRouteBuilder";
    public static final String ROUTE_ID_FROM_JSON = "ShoppingListFromJsonRouteBuilder";

    @Override
    public void configure() {
        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .handled(true);

        from(ENTRY_SEDA_ENDOINT_URI)
                .routeId(ROUTE_ID)
                .marshal().json()
                .to("log:best_shoppingList_json_entry")
                //file will be overwritten,can be avoided with "fileExists=append"
                .to(FILE_LOGFILE_ENDPOINT);

        from("direct:triggerFileRoute")
                .routeId("triggerFileRoute")
                .log("test")
                .to(FILE_LOGFILE_ENDPOINT);

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
