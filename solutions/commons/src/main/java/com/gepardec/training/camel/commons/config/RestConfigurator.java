package com.gepardec.training.camel.commons.config;

import com.gepardec.training.camel.commons.domain.Order;
import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
public final class RestConfigurator extends RouteBuilder {

    @Override
    public void configure() {

        restConfiguration().component("servlet").bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");

        restConfiguration().component("netty-http").host("localhost").port(8080).bindingMode(RestBindingMode.auto);
    }
}
