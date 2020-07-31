package com.gepardec.training.camel.best;

import com.gepardec.training.camel.best.config.Endpoints;
import com.gepardec.training.camel.best.config.ExchangeHeaders;
import com.gepardec.training.camel.best.domain.OrderItem;
import com.gepardec.training.camel.best.misc.OrderSplitter;
import com.gepardec.training.camel.best.misc.OrderTransformer;
import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class SplitterRouteBuilder extends RouteBuilder {

    @Override
    public void configure() {
        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .handled(true);

        from(Endpoints.SPLITTER_ENTRY_SEDA_ENDPOINT.endpointUri())
                .setHeader(ExchangeHeaders.PARTNER_ID, simple("${body.partnerId}"))
                .split().method(OrderSplitter.class).streaming()
                .bean(OrderTransformer.class)
                .removeHeader(ExchangeHeaders.PARTNER_ID)
                .choice()
                    .when(hasItemCode(OrderItem.EGG))
                        .to(Endpoints.EGG_ORDER_ENTRY_SEDA_ENDPOINT.endpointUri())
                    .when(hasItemCode(OrderItem.PASTA))
                        .to(Endpoints.PASTA_ORDER_ENTRY_SEDA_ENDPOINT.endpointUri())
                    .when(hasItemCode(OrderItem.MILK))
                        .to(Endpoints.MILK_ORDER_ENTRY_SEDA_ENDPOINT.endpointUri())
                    .when(hasItemCode(OrderItem.MEAT))
                        .to(Endpoints.MEAT_ORDER_ENTRY_SEDA_ENDPOINT.endpointUri())
                    .otherwise()
                        .log("ERROR...")
                .end();

    }

    public Predicate hasItemCode(long itemCode) {
        return simple("${body.code} == " + itemCode);
    }
}
