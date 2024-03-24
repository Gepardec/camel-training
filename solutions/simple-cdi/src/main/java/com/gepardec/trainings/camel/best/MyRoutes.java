package com.gepardec.trainings.camel.best;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;

import com.gepardec.training.camel.commons.domain.Order;


public class MyRoutes extends RouteBuilder {

	public static final String DIRECT_BEAN_OTHER = "direct:beanOther";
	public static final String DIRECT_ORDER_IN = "direct:order_in";
	public static final String URL_FILE_ORDERS_IN = "file:target/orders/in";
	public static final String URL_FILE_ORDERS_OUT = "file:target/orders/processed";


    @Override
    public void configure() {
    	JacksonDataFormat orderFormat = new JacksonDataFormat(Order.class);

        from(URL_FILE_ORDERS_IN).routeId(URL_FILE_ORDERS_IN)
        .to(DIRECT_ORDER_IN);
        
        from(DIRECT_ORDER_IN)        
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
        .to(DIRECT_BEAN_OTHER);
        
        from(DIRECT_BEAN_OTHER).routeId(MyRoutes.DIRECT_BEAN_OTHER)
        .log("Send to file: ${body}")
        .to(MyRoutes.URL_FILE_ORDERS_OUT);
 
    }
}
