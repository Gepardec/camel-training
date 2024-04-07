package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.domain.OrderToProducer;
import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JaxbDataFormat;

public final class EggOrderRouteBuilder extends RouteBuilder {

    public static final String ENTRY_SEDA_ENDOINT_URI = "seda://egg_order_entry";
    public static final String OUTPUT_JMS_ENDPOINT_URI = "jms://queue:eggs";

    @Override
    public void configure() {
        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .handled(true);

        JaxbDataFormat xml = new JaxbDataFormat();

        from(ENTRY_SEDA_ENDOINT_URI).routeId(ENTRY_SEDA_ENDOINT_URI)
                .to("log:filter OrderToProducer")
                .filter(body().isInstanceOf(OrderToProducer.class))
                .marshal(xml)
                .log("Sending ${body} to " + OUTPUT_JMS_ENDPOINT_URI)
                .setExchangePattern(ExchangePattern.InOnly)
                .to(OUTPUT_JMS_ENDPOINT_URI);
    }
}
