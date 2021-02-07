package com.gepardec.trainings.camel.best;

import javax.inject.Inject;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.Uri;
import org.apache.camel.component.jackson.JacksonDataFormat;

import com.gepardec.training.camel.commons.domain.Order;


public class MyRoutes extends RouteBuilder {

	public static final String DIRECT_BEAN_OTHER = "direct:beanOther";
	public static final String OTHER_BEAN_ROUTE = "OTHER_BEAN_ROUTE";
	public static final String DIRECT_ORDER_IN = "direct:order_in";
	public static final String URL_FILE_ORDERS_IN = "file:src/orders";
	public static final String URL_FILE_ORDERS_OUT = "file:target/orders/processed";

    @Inject
    @Uri(DIRECT_BEAN_OTHER)
    private Endpoint directSomeBean;

    @Inject
    @Uri(MyRoutes.DIRECT_ORDER_IN)
    private Endpoint fromEndpoint;


    @Override
    public void configure() {
    	JacksonDataFormat orderFormat = new JacksonDataFormat(Order.class);

        from(URL_FILE_ORDERS_IN).routeId(URL_FILE_ORDERS_IN)
        .to(fromEndpoint);
        
        from(fromEndpoint)        
        .log("Process order ${body}")
        .unmarshal(orderFormat)
        .process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				exchange.getIn().getBody(Order.class).setPartnerId(34);
			}
		})
        .bean("someBean")
        .marshal(orderFormat)
        .log("Processed order ${body}")
        .to(directSomeBean);
        
        from(directSomeBean).routeId(MyRoutes.DIRECT_BEAN_OTHER)
        .log("Send to file: ${body}")
        .to(MyRoutes.URL_FILE_ORDERS_OUT);
 
    }
}
