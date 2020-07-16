package com.gepardec.training.camel.best;

import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class PastaOrderRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from(Endpoints.PASTA_ORDER_ENTRY_SEDA_ENDPOINT.endpointUri())
                .log("PASTA ${body}");
    }
}
