package com.gepardec.trainings.camel.best;

import javax.inject.Inject;

import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.Uri;


public class MyRoutes extends RouteBuilder {

    public static final String URL_FILE_ORDERS_IN = "file:src/orders?noop=true";
    public static final String URL_FILE_ORDERS_OUT = "file:target/orders/processed";
    public static final String DIRECT_FROM_ROUTE = "directFromRoute";
	public static final String URL_DIRECT_ORDER_IN = "direct:fileOrdersIn";
	

    @Inject
    @Uri(MyRoutes.URL_DIRECT_ORDER_IN)
    private Endpoint fromEndpoint;
    
    @Override
    public void configure() {

        from(URL_FILE_ORDERS_IN)
        .to(fromEndpoint);
        
        from(fromEndpoint).routeId(MyRoutes.DIRECT_FROM_ROUTE)     
        .log("Process order ${body}")
        .to(URL_FILE_ORDERS_OUT);
 
    }
}
