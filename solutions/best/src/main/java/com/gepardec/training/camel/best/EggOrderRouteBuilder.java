package com.gepardec.training.camel.best;

import com.gepardec.training.camel.best.config.Endpoints;
import com.gepardec.training.camel.best.domain.OrderToProducer;
import com.gepardec.training.camel.commons.endpoint.CamelEndpoint;
import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.Uri;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.springframework.context.annotation.Bean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public final class EggOrderRouteBuilder extends RouteBuilder {

    public static final String ENTRY_SEDA_ENDOINT_URI = "seda:egg_order_entry";
    public static final String ENTRY_SEDA_ENDOINT_ID = "egg_order_entry";

    public static final String OUTPUT_JMS_ENDPOINT_URI = "jms:queue:eggs?disableReplyTo=true&username=quarkus&password=quarkus&connectionFactory=#JMSConnectionFactory";
    public static final String OUTPUT_JMS_ENDPOINT_ID = "jms_queue_eggs";

    public static final String ROUTE_ID = "EggOrderRouteBuilder";

    @Inject
    @Uri(ENTRY_SEDA_ENDOINT_URI)
    private Endpoint entryEndpoint;

    @Inject
    @Uri(OUTPUT_JMS_ENDPOINT_URI)
    private Endpoint jmsEndpoint;

    @Bean
    public JmsComponent jms() {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
        cf.setBrokerURL("tcp://localhost:61616");
        cf.setUserName("quarkus");
        cf.setPassword("quarkus");

        return JmsComponent.jmsComponentAutoAcknowledge(cf);
    }

    @Override
    public void configure() {
        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .handled(true);

        JaxbDataFormat format = new JaxbDataFormat();

        from(entryEndpoint).id(ENTRY_SEDA_ENDOINT_ID)
                .routeId(ROUTE_ID)
                .filter(body().isInstanceOf(OrderToProducer.class))
                .marshal(format)
                .log("Sending ${body} to " + OUTPUT_JMS_ENDPOINT_URI)
                .to(jmsEndpoint).id(OUTPUT_JMS_ENDPOINT_ID);
    }
}
