package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.domain.Order;
import jakarta.ws.rs.core.Response;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

public final class EntryRouteBuilder extends RouteBuilder {

    public static final String ORDER_FILE_ENDOINT_URI = "file:target/messages/order";
    public static final String ENTRY_DIRECT_ENDOINT_URI = "direct://order_entry";
    public static final String ENTRY_REST_ENDOINT_ROUTE_ID = "rest://order_entry";

    @Override
    public void configure() {

        restConfiguration().bindingMode(RestBindingMode.json);

        rest("/best/")
        	.post().routeId(ENTRY_REST_ENDOINT_ROUTE_ID)
        	.type(Order.class)
        	.to(ENTRY_DIRECT_ENDOINT_URI);

        from(ENTRY_DIRECT_ENDOINT_URI).routeId(ENTRY_DIRECT_ENDOINT_URI)
        	.to("log:ENTRY_DIRECT_ENDOINT_URI?level=INFO")
        	.marshal().json(true)
        	.to(ORDER_FILE_ENDOINT_URI)
        	.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(Response.Status.ACCEPTED.getStatusCode()))
        	.setBody(constant("OK"));

    }
}
