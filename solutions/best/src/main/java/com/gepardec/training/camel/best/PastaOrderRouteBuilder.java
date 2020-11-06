package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.Uri;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.sql.SQLException;

@ApplicationScoped
public final class PastaOrderRouteBuilder extends RouteBuilder {

    public static final String ENTRY_SEDA_ENDOINT_URI = "seda://pasta_order_entry";
    public static final String OUTPUT_SQL_ENDPOINT_URI = "sql://insert into order_to_producer (id, partner_id, item_code, item_count) values (:#${bean:idGenerator.nextId}, :#${body.partnerId}, :#${body.code}, :#${body.amount})?dataSource=#postgres";


    @Inject
    @Uri(ENTRY_SEDA_ENDOINT_URI)
    private Endpoint entryEndpoint;

    @Inject
    @Uri(OUTPUT_SQL_ENDPOINT_URI)
    private Endpoint sqlEndpoint;

    @Override
    public void configure() throws IOException, SQLException {

        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .handled(true);

        from(entryEndpoint).routeId(ENTRY_SEDA_ENDOINT_URI)
                .to(sqlEndpoint);
    }

}
