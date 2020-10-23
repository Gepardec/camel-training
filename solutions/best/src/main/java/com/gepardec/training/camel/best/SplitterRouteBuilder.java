package com.gepardec.training.camel.best;

import com.gepardec.training.camel.best.domain.OrderItem;
import com.gepardec.training.camel.best.misc.OrderSplitter;
import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.camel.Endpoint;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.Uri;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public final class SplitterRouteBuilder extends RouteBuilder {

    public static final String ENTRY_SEDA_ENDOINT_URI = "seda:best_splitter_entry";
    public static final String ENTRY_SEDA_ENDOINT_ID = "best_splitter_entry";

    public static final String ROUTE_ID = "SplitterRouteBuilder";

    @Inject
    @Uri(EggOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
    Endpoint eggEndpoint;

    @Inject
    @Uri(MeatOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
    Endpoint meatEndpoint;

    @Inject
    @Uri(PastaOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
    Endpoint pastaEndpoint;

    @Inject
    @Uri(MilkOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
    Endpoint milkEndpoint;

    @Override
    public void configure() {
        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .handled(true);

        //@formatter:off
        from(ENTRY_SEDA_ENDOINT_URI).id(ENTRY_SEDA_ENDOINT_ID)
                .routeId(ROUTE_ID)
                .split().method(OrderSplitter.class)
                .log("! ${body}")
                .to("direct:choice").id("bla");


        from("direct:choice").id("bla").routeId("Blubb").log("!! ${body}")
                .choice()
                    .when(hasItemCode(OrderItem.EGG))
                        .to(eggEndpoint).id(EggOrderRouteBuilder.ENTRY_SEDA_ENDOINT_ID)
                    .when(hasItemCode(OrderItem.PASTA))
                        .to(pastaEndpoint).id(PastaOrderRouteBuilder.ENTRY_SEDA_ENDOINT_ID)
                    .when(hasItemCode(OrderItem.MILK))
                        .to(milkEndpoint).id(MilkOrderRouteBuilder.ENTRY_SEDA_ENDOINT_ID)
                    .when(hasItemCode(OrderItem.MEAT))
                        .to(meatEndpoint).id(MeatOrderRouteBuilder.ENTRY_SEDA_ENDOINT_ID)
                .otherwise()
                    .log("ERROR...")
                .end();
        //@formatter:on


    }

    public Predicate hasItemCode(long itemCode) {
        return simple("${body.code} == " + itemCode);
    }
}
