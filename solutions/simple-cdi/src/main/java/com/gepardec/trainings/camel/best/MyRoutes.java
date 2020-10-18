package com.gepardec.trainings.camel.best;

import javax.inject.Inject;

import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.Uri;


public class MyRoutes extends RouteBuilder {

    public static final String URL_FILE_ORDERS_IN = "file:src/orders?noop=true";
	public static final String URL_FILE_ORDERS_OUT = "file:target/orders/processed";

    @Inject
    @Uri(URL_FILE_ORDERS_IN)
    private Endpoint inputEndpoint;

    @Inject
    @Uri(URL_FILE_ORDERS_OUT)
    private Endpoint resultEndpoint;

    @Override
    public void configure() {
        from(inputEndpoint)
        .to(resultEndpoint);
    }
}
