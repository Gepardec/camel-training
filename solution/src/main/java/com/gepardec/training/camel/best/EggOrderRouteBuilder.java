package com.gepardec.training.camel.best;

import com.gepardec.training.camel.best.domain.OrderItem;
import com.gepardec.training.camel.best.domain.OrderSplitter;
import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class EggOrderRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from(Endpoints.EGG_ORDER_ENTRY_SEDA_ENDPOINT.endpointUri())
                .log("EGGS ${body}");
    }
}
