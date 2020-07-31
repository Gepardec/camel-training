package com.gepardec.training.camel.best;

import com.gepardec.training.camel.best.config.Endpoints;
import com.gepardec.training.camel.best.misc.IdGenerator;
import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public final class PastaOrderRouteBuilder extends RouteBuilder {

    @Inject
    CamelContext camelContext;

    @Override
    public void configure() {

        camelContext.getRegistry().bind("idGenerator", new IdGenerator());

        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .handled(true);

        from(Endpoints.PASTA_ORDER_ENTRY_SEDA_ENDPOINT.endpointUri())
                .to(Endpoints.PASTA_ORDER_SQL_ENDPOINT.endpointUri());
    }

}
