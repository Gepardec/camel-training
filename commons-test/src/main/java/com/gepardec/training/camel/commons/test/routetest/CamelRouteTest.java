package com.gepardec.training.camel.commons.test.routetest;


import com.gepardec.training.camel.commons.endpoint.CamelEndpoint;
import com.gepardec.training.camel.commons.test.TestBase;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.rest.RestEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CamelRouteTest extends TestBase {

    public static final Logger LOGGER = LoggerFactory.getLogger(CamelRouteTest.class);

    protected Exchange exchange;
    protected CamelContext camelContext;
    private RouteBuilder routeUnderTest;
    private final Map<String, PollingConsumer> consumers = new HashMap<>();

    @BeforeEach
    public void defaultSetup() throws Exception {

        camelContext = new DefaultCamelContext();
        exchange = new DefaultExchange(camelContext);

        retrieveRouteUnderTest();
        configureMockEndpoints(true);

        if (routeUnderTest == null) {
            throw new RuntimeException("Route under test not declared");
        }

        camelContext.addRoutes(routeUnderTest);
        camelContext.start();
    }

    @AfterEach
    public void defaultShutdown() throws Exception {
        configureMockEndpoints(false);
    }

    protected void configureMockEndpoints(boolean activate) throws Exception {
        if (!activate) {
            CamelEndpoint.setTestMode(false);
            return;
        }
        CamelEndpoint.setTestMode(true);
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(MockedEndpoint.class)) {
                field.setAccessible(true);
                Object endpoint = field.get(this);
                if (endpoint instanceof CamelEndpoint) {
                    String id = ((CamelEndpoint) endpoint).getId();
                    PollingConsumer pollingConsumer = consumers.get(id);
                    if (pollingConsumer == null) {
                        Endpoint pollingEndpoint = camelContext.getEndpoint(((CamelEndpoint) endpoint).endpointUri());
                        pollingConsumer = pollingEndpoint.createPollingConsumer();
                        pollingConsumer.start();
                        consumers.put(id, pollingConsumer);
                    }
                }
            }
        }
    }

    protected void retrieveRouteUnderTest() throws IllegalAccessException {
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(RouteUnderTest.class)) {
                field.setAccessible(true);
                Object route = field.get(this);
                if (route instanceof RouteBuilder) {
                    this.routeUnderTest = (RouteBuilder) route;
                }
            }
        }
    }

    /**
     * Sends body object to endpoint
     */
    protected void sendToEndpoint(String endpoint, Object body) {
        try (ProducerTemplate producer = camelContext.createProducerTemplate()) {
            producer.sendBody(endpoint, body);
        } catch (IOException e) {
            LOGGER.error("Exception occured: ", e);
        }
    }

    /**
     * Sends body object to endpoint
     */
    protected void sendToEndpoint(CamelEndpoint endpoint, Object body) {
        sendToEndpoint(endpoint.endpointUri(), body);
    }

    /**
     * Sends body object and headers to endpoint
     */
    protected void sendToEndpoint(CamelEndpoint endpoint, Object body, final Map<String, Object> headers) {


        try (ProducerTemplate producerTemplate = camelContext.createProducerTemplate()) {
            producerTemplate.sendBodyAndHeaders(endpoint.endpointUri(), body, headers);
        } catch (IOException e) {
            LOGGER.error("Exception occured: ", e);
        }
    }

    /**
     * Polls from endpoint. It waits 3 seconds for result. If there is nothing to poll within 3 seconds, <tt>null</tt> is returned.
     */
    protected Exchange pollFromEndpoint(final CamelEndpoint endpoint) {
        return pollFromEndpoint(endpoint, 3000);
    }

    /**
     * Polls from endpoint. It waits given timeout in milliseconds for result. If there is nothing to poll within timeout, <tt>null</tt> is returned.
     */
    protected Exchange pollFromEndpoint(final CamelEndpoint endpoint, final long timeOut) {
        return consumers.get(endpoint.getId()).receive(timeOut);
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface RouteUnderTest {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface MockedEndpoint {
    }

}
