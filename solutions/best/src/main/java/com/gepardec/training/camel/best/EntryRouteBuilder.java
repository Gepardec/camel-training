package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.domain.Order;
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

        rest("/best/")
                .post()
                .consumes(MediaType.APPLICATION_JSON)
                .type(Order.class)
                .id("best_rest")
                .route().to(SplitterRouteBuilder.SPLITTER_FROM_ENDOINT_URI)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(Response.Status.ACCEPTED.getStatusCode()))
                .endRest();

    }
}
