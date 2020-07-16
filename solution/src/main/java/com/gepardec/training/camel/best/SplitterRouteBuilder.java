package com.gepardec.training.camel.best;

import com.gepardec.training.camel.best.domain.Order;
import com.gepardec.training.camel.best.domain.OrderItem;
import com.gepardec.training.camel.best.domain.OrderSplitter;
import com.gepardec.training.camel.commons.endpoint.CamelEndpoint;
import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class SplitterRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .handled(true);

        from(Endpoints.SPLITTER_ENTRY_SEDA_ENDPOINT.endpointUri())
                .split().method(OrderSplitter.class).streaming()
                .choice()
                    .when(hasItemCode(OrderItem.EGG))
                        .to(Endpoints.EGG_ORDER_ENTRY_SEDA_ENDPOINT.endpointUri())
                    .when(hasItemCode(OrderItem.PASTA))
                        .to(Endpoints.PASTA_ORDER_ENTRY_SEDA_ENDPOINT.endpointUri())
                    .when(hasItemCode(OrderItem.MILK))
                        .to(Endpoints.MILK_ORDER_ENTRY_SEDA_ENDPOINT.endpointUri())
                    .when(hasItemCode(OrderItem.MEAT))
                        .to(Endpoints.MEAT_ORDER_ENTRY_SEDA_ENDPOINT.endpointUri())
                .end();

    }

    public Predicate hasItemCode(long itemCode){
        return simple("${body.code} == " + itemCode);
    }
}
