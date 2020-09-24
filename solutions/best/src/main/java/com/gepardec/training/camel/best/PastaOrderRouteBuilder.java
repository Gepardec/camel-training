package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.sql.SQLException;

@ApplicationScoped
public final class PastaOrderRouteBuilder extends RouteBuilder {

    public static final String ENTRY_SEDA_ENDOINT_URI = "seda:pasta_order_entry";
    public static final String ENTRY_SEDA_ENDOINT_ID = "pasta_order_entry";

    public static final String OUTPUT_SQL_ENDPOINT_URI = "sql:insert into order_to_producer (id, partner_id, item_code, item_count) values (:#${bean:idGenerator.nextId}, :#${body.partnerId}, :#${body.code}, :#${body.amount})?dataSource=#postgres";
    public static final String OUTPUT_SQL_ENDPOINT_ID = "sql_pasta";

    public static final String ROUTE_ID = "PastaOrderRouteBuilder";

    @Override
    public void configure() throws IOException, SQLException {

        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .handled(true);

        from(ENTRY_SEDA_ENDOINT_URI)
                .routeId(ROUTE_ID)
                .to(OUTPUT_SQL_ENDPOINT_URI).id(OUTPUT_SQL_ENDPOINT_ID);
    }

}
