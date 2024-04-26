package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.domain.Order;
import jakarta.ws.rs.core.Response;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

public final class EntryRouteBuilder extends RouteBuilder {

    public static final String ORDER_ENDOINT_URI = "file:target/messages/order";
    public static final String ENTRY_DIRECT_ENDOINT_URI = "direct://entry_egg_order_entry";
    public static final String ENTRY_REST_ENDOINT_ROUTE_ID = "rest://entry_egg_order_entry";

    @Override
    public void configure() {

        restConfiguration().bindingMode(RestBindingMode.json);

        rest("/best/")
                .post().routeId(ENTRY_REST_ENDOINT_ROUTE_ID)
                .type(Order.class)
                .id("best_rest")
                .to(ENTRY_DIRECT_ENDOINT_URI);

        from(ENTRY_DIRECT_ENDOINT_URI).routeId(ENTRY_DIRECT_ENDOINT_URI)
                .to("log:ENTRY_DIRECT_ENDOINT_URI?level=INFO")
                //                .to(ORDER_ENDOINT_URI)
                .to(SplitterRouteBuilder.SPLITTER_FROM_ENDOINT_URI)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(Response.Status.ACCEPTED.getStatusCode()));

    }
}
