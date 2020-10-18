package com.gepardec.trainings.camel.best;

import javax.inject.Inject;

import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ImportResource;
import org.apache.camel.cdi.Uri;

/**
 * Configures all our Camel routes, components, endpoints and beans
 */
@ImportResource("camel-context.xml")
public class MyRoutes extends RouteBuilder {

    @Inject
    @Uri("timer:foo?period=5000")
    private Endpoint inputEndpoint;

    @Inject
    @Uri("log:java_route")
    private Endpoint resultEndpoint;

    @Override
    public void configure() {
        // you can configure the route rule with Java DSL here

        from(inputEndpoint)
            .to("bean:counterBean")
            .to(resultEndpoint);
    }

}
