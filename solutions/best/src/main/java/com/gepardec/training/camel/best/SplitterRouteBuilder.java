package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.domain.OrderItem;
import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.camel.Endpoint;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.Uri;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public final class SplitterRouteBuilder extends RouteBuilder {

    public static final String SPLITTER_FROM_ENDOINT_URI = "seda://best_splitter_from";
    public static final String CHOICE_FROM_ENDOINT_URI = "direct://best_choice_from";

    @Inject
    @Uri(CHOICE_FROM_ENDOINT_URI)
    Endpoint choiceEndpoint;

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

        from(SPLITTER_FROM_ENDOINT_URI).routeId(SPLITTER_FROM_ENDOINT_URI)
                .split().method(OrderSplitter.class)
                    .log(simple("${header.CamelSplitIndex}").getText())
                    .to(choiceEndpoint)
                .end();

        from(choiceEndpoint).routeId(CHOICE_FROM_ENDOINT_URI)
                .choice()
                    .when(hasItemCode(OrderItem.EGG))
                        .to(eggEndpoint)
                    .when(hasItemCode(OrderItem.PASTA))
                        .to(pastaEndpoint)
                    .when(hasItemCode(OrderItem.MILK))
                        .to(milkEndpoint)
                    .when(hasItemCode(OrderItem.MEAT))
                        .to(meatEndpoint)
                .otherwise()
                    .log("ERROR...")
                .end();
        //@formatter:on


    }

    public Predicate hasItemCode(long itemCode) {
        return simple("${body.code} == " + itemCode);
    }
}
