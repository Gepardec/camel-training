package com.gepardec.trainings.camel.best;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;

import com.gepardec.training.camel.best.domain.Order;


public class MyRoutes extends AdviceWithRouteBuilder {

	public static final String DIRECT_ORDER_IN = "direct:order_in";
	public static final String URL_FILE_ORDERS_IN = "file:src/orders";
	public static final String URL_FILE_ORDERS_OUT = "file:target/orders/processed";

 //   @Inject
 //   @Uri(URL_FILE_ORDERS_IN)
    private Endpoint inputEndpoint;

//    @Inject
//    @Uri(URL_FILE_ORDERS_OUT)
    private Endpoint resultEndpoint;

    @Override
    public void configure() {
    	JacksonDataFormat orderFormat = new JacksonDataFormat(Order.class);

        from(URL_FILE_ORDERS_IN)
        .log("Process order ${body}")
        .unmarshal(orderFormat)
        .to(DIRECT_ORDER_IN);
        
        from(DIRECT_ORDER_IN)
        .process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				exchange.getIn().getBody(Order.class).setPartnerId(34);
			}
		})
        .marshal(orderFormat)
        .log("Processed order ${body}")
        .to(URL_FILE_ORDERS_OUT);
    }
}
