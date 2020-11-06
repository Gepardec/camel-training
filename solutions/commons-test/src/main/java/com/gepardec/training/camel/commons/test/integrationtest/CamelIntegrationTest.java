package com.gepardec.training.camel.commons.test.integrationtest;


import com.gepardec.training.camel.commons.test.TestBase;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CamelIntegrationTest extends TestBase {

    protected Exchange exchange;
    protected CamelContext camelContext;
    private RouteBuilder routeUnderTest;

    private final static Logger LOG = LoggerFactory.getLogger(CamelIntegrationTest.class);
    private ConsumerTemplate consumerTemplate;

    @Before
    public void defaultSetup() throws Exception {
        camelContext = new DefaultCamelContext();
        exchange = new DefaultExchange(camelContext);
        camelContext.start();
    }

    @After
    public void defaultShutdown() throws Exception {
        if (consumerTemplate != null) {
            consumerTemplate.stop();
        }
    }

    /**
     * Polls message from the endpoint. If it is a message queue endpoint, the message will be polled from the real queue.
     */
    protected Exchange pollFromEndpoint(final String endpoint) {
        return pollFromEndpoint(endpoint, 10000);
    }

    /**
     * Polls message from the endpoint. If it is a message queue endpoint, the message will be polled from the real queue.
     */
    protected Exchange pollFromEndpointNegativeTest(final String endpoint) {
        return pollFromEndpoint(endpoint, 5000);
    }



    /**
     * Polls message from the endpoint. If it is a message queue endpoint, the message will be polled from the real queue.
     */
    protected Exchange pollFromEndpoint(final String endpoint, final long timeOut) {
        if (consumerTemplate == null) {
            consumerTemplate = camelContext.createConsumerTemplate();
        }

        return consumerTemplate.receive(endpoint, timeOut);
    }

    protected void clearEndpointQueue(final String endpoint, final int timeoutInMs) {
        final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        final Runnable runnable = () -> {
            pollFromEndpoint(endpoint, 1000);
        };
        final ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
        try {
            scheduledFuture.get(timeoutInMs, TimeUnit.MILLISECONDS);
        } catch (Exception ignore) {
        }

        executor.shutdown();
    }

    protected void clearEndpointQueue(final String endpoint) {
        clearEndpointQueue(endpoint, 2000);
    }
}
