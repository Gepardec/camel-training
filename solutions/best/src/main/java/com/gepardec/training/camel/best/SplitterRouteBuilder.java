package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.domain.OrderItem;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;

public final class SplitterRouteBuilder extends RouteBuilder {

    public static final String SPLITTER_FROM_ENDOINT_URI = "seda://best_splitter_from";
    public static final String CHOICE_FROM_ENDOINT_URI = "direct://best_choice_from";

    @Override
    public void configure() {

        //@formatter:off

        from(SPLITTER_FROM_ENDOINT_URI).routeId(SPLITTER_FROM_ENDOINT_URI)
                .split().method(OrderSplitter.class)
                .log("CamelSplitIndex/CamelSplitSize: ${header.CamelSplitIndex}/${header.CamelSplitSize}") 
                .to(CHOICE_FROM_ENDOINT_URI) // Todo Streaming???
                .end();

        from(CHOICE_FROM_ENDOINT_URI).routeId(CHOICE_FROM_ENDOINT_URI)
                .choice()
                .when(hasItemCode(OrderItem.EGG))
                .log("Got egg")
                .to(EggOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
                .when(hasItemCode(OrderItem.MEAT))
                .log("Got meat")
                .to(MeatOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
                .when(hasItemCode(OrderItem.MILK))
                .log("Got milk")
                .to(MilkOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
                .when(hasItemCode(OrderItem.PASTA))
                .log("Got pasta")
                .to(PastaOrderRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
                .otherwise()
                .log("ERROR...")
                .end();
        //@formatter:on

    }

    public Predicate hasItemCode(long itemCode) {
        return simple("${body.code} == " + itemCode);
    }
}
