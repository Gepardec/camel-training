package com.gepardec.training.camel.best;

import com.gepardec.training.camel.best.config.ConfigurationUtils;
import com.gepardec.training.camel.best.config.Endpoints;
import com.gepardec.training.camel.best.domain.OrderToProducer;
import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JaxbDataFormat;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public final class EggOrderRouteBuilder extends RouteBuilder {

    @Inject
    private CamelContext camelContext;

    @Override
    public void configure() throws Exception {

        ConfigurationUtils.setupJmsConnectionFactory(camelContext, "tcp://localhost:61616", "quarkus", "quarkus");

        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .handled(true);

        JaxbDataFormat format = new JaxbDataFormat();

        from(Endpoints.EGG_ORDER_ENTRY_SEDA_ENDPOINT.endpointUri())
                .filter(body().isInstanceOf(OrderToProducer.class))
                .marshal(format)
                .log("Sending ${body} to " + Endpoints.EGG_ORDER_JMS_ENDPOINT.endpointUri())
                .to(Endpoints.EGG_ORDER_JMS_ENDPOINT.endpointUri());
    }
}
