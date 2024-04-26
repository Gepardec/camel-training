package com.gepardec.training.camel.best;

import org.apache.camel.builder.RouteBuilder;

/**
 * Camel route definitions.
 */
public class FileRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("file:target/messages/in").routeId("FileRouteId")
                .setBody(simple("Hello ${body}!"))
                .to("file:target/messages/out");
    }

}
