package com.gepardec.training.camel.best;

import com.gepardec.training.camel.best.config.Endpoints;
import com.gepardec.training.camel.best.domain.Order;
import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
public final class EntryRouteBuilder extends RouteBuilder {

    @Override
    public void configure() {

        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .handled(true);

        restConfiguration().component("servlet").bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");

        rest("/best/")
                .post()
                .consumes(MediaType.APPLICATION_JSON)
                .type(Order.class)
                .id("best_rest")
                .route().to(Endpoints.SPLITTER_ENTRY_SEDA_ENDPOINT.endpointUri())
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(Response.Status.ACCEPTED.getStatusCode()))
                .endRest();

    }
}
