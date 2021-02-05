package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.processor.ExceptionLoggingProcessor;
import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.Uri;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.sql.SQLException;

@ApplicationScoped
public final class PastaOrderRouteBuilder extends RouteBuilder {

    public static final String ENTRY_SEDA_ENDOINT_URI = "seda://pasta_order_entry";


    @Inject
    @Uri(ENTRY_SEDA_ENDOINT_URI)
    private Endpoint entryEndpoint;

    @Override
    public void configure() {

        onException(Exception.class)
                .process(new ExceptionLoggingProcessor())
                .handled(true);

        from(entryEndpoint).routeId(ENTRY_SEDA_ENDOINT_URI)
                .log("PASTA ${body}");
    }

}
