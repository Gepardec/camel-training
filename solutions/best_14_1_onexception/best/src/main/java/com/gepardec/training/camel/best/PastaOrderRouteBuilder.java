package com.gepardec.training.camel.best;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.camel.builder.RouteBuilder;

public final class PastaOrderRouteBuilder extends RouteBuilder {

    public static final String ENTRY_DIRECT_ENDOINT_URI = "direct://pasta_order_entry";
    public static final String OUTPUT_SQL_ENDPOINT_URI = "sql://insert into order_to_producer (id, partner_id, item_code, item_count) values (CAST(:#${bean:uuidGen.nextId} AS uuid), :#${body.partnerId}, :#${body.code}, :#${body.amount})";
    public static final String OUTPUT_SQL_ENDPOINT_URI2 = "sql://insert into order_to_producer (id, partner_id, item_code, item_count) values (CAST(:#${header.bestDbKey} AS uuid), :#${body.partnerId}, :#${body.code}, :#${body.amount})";

    @Override
    public void configure() throws IOException, SQLException {

        from(ENTRY_DIRECT_ENDOINT_URI).routeId(ENTRY_DIRECT_ENDOINT_URI)
        	.setHeader("bestDbKey", simple("${bean:uuidGen.nextId}"))
        	.log(OUTPUT_SQL_ENDPOINT_URI2)
        	.to(OUTPUT_SQL_ENDPOINT_URI2);

    }

}
