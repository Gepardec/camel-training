package com.gepardec.training.camel.best;

import com.gepardec.training.camel.best.domain.Order;
import com.gepardec.training.camel.commons.endpoint.CamelEndpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class EntryRouteBuilder extends RouteBuilder {

    public static final CamelEndpoint JSON_VALIDATOR_ENDPOINT = new CamelEndpoint("json-validator:json/schema/order.json", "json_validator_order");

    @Override
    public void configure() throws Exception {

        restConfiguration().component("servlet").bindingMode(RestBindingMode.json)
                // and output using pretty print
                .dataFormatProperty("prettyPrint", "true");
                // setup context path and port number that Apache Tomcat will deploy
                // this application with, as we use the servlet component, then we
                // need to aid Camel to tell it these details so Camel knows the url
                // to the REST services.
                // Notice: This is optional, but needed if the RestRegistry should
                // enlist accurate information. You can access the RestRegistry
                // from JMX at runtime
                //.contextPath("/").port(8080);

        rest("/best/").post().to(Endpoints.ENTRY_DIRECT_ENDPOINT.endpointUri());

        // JSON Data Format
        JacksonDataFormat jsonDataFormat = new JacksonDataFormat(Order.class);

        from(Endpoints.ENTRY_DIRECT_ENDPOINT.endpointUri())
                .to(JSON_VALIDATOR_ENDPOINT.endpointUri())
                .unmarshal(jsonDataFormat)
                .to(Endpoints.SPLITTER_ENTRY_SEDA_ENDPOINT.endpointUri());
    }
}
