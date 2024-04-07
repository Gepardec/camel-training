package com.gepardec.training.camel.best;

import java.io.IOException;
import java.sql.SQLException;

import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.camel.builder.RouteBuilder;

public final class PastaOrderRouteBuilder extends RouteBuilder {

    public static final String ENTRY_SEDA_ENDOINT_URI = "direct://pasta_order_entry";
    public static final String OUTPUT_SQL_ENDPOINT_URI = "sql://insert into order_to_producer (id, partner_id, item_code, item_count) values (CAST(:#${exchangeProperty.BestDbID} AS uuid), :#${body.partnerId}, :#${body.code}, :#${body.amount})?dataSource=#my_db";
    protected static final String OUTPUT_FILE_ENDPOINT_URI = "file:target/messages/pasta?delete=true";

    @Override
    public void configure() throws IOException, SQLException {

        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .handled(true);

        from(ENTRY_SEDA_ENDOINT_URI).routeId(ENTRY_SEDA_ENDOINT_URI)
                .setProperty("BestDbID", simple("${bean:idGenerator.nextId}"))
                .log(OUTPUT_SQL_ENDPOINT_URI)
                .to(OUTPUT_SQL_ENDPOINT_URI)
                .to(OUTPUT_FILE_ENDPOINT_URI);

    }

}
