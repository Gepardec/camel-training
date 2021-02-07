package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.domain.OrderToProducer;
import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.Endpoint;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.Uri;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.springframework.context.annotation.Bean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public final class EggOrderRouteBuilder extends RouteBuilder {

    public static final String ENTRY_SEDA_ENDOINT_URI = "seda://egg_order_entry";
    public static final String OUTPUT_JMS_ENDPOINT_URI = "jms://queue:eggs";
    public static final String OUTPUT_JMS_ENDPOINT_ID = "jms_eggs";

    @Inject
    @Uri(ENTRY_SEDA_ENDOINT_URI)
    private Endpoint entryEndpoint;

    @Inject
    @Uri(OUTPUT_JMS_ENDPOINT_URI)
    private Endpoint jmsEndpoint;

    @Override
    public void configure() {
        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .handled(true);

        JaxbDataFormat xml = new JaxbDataFormat();

        from(entryEndpoint).routeId(ENTRY_SEDA_ENDOINT_URI)
                .filter(body().isInstanceOf(OrderToProducer.class))
                .marshal(xml)
                .log("Sending ${body} to " + OUTPUT_JMS_ENDPOINT_URI)
                .setExchangePattern(ExchangePattern.InOnly)
                .to(jmsEndpoint).id(OUTPUT_JMS_ENDPOINT_ID);
    }
}
