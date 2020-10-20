package com.gepardec.training.camel.commons.test.routetest;


import com.gepardec.training.camel.commons.endpoint.CamelEndpoint;
import com.gepardec.training.camel.commons.test.TestBase;
import org.apache.camel.*;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.cdi.Uri;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CamelRouteCDITest extends TestBase {

    public static final Logger LOGGER = LoggerFactory.getLogger(CamelRouteCDITest.class);

    private static boolean weaved;

    @Inject
    protected CamelContext context;

    @Before
    public void defaultSetup() throws Exception {
        if(!weaved){
            weaveEndpoints(false);
            weaved = true;
        }
    }

    @BeforeClass
    public static void defaultSetupClass() throws Exception {
        weaved = false;
    }

    @After
    public void defaultTeardown() throws Exception {
    }

    @AfterClass
    public static void afterSetupClass() throws Exception {
        weaved = false;
    }

    protected void weaveEndpoints(boolean teardown) throws Exception {
        MockedRouteId mockedRouteId = this.getClass().getAnnotation(MockedRouteId.class);
        if(mockedRouteId == null){
            return;
        }
        AdviceWithRouteBuilder.adviceWith(context, mockedRouteId.value(), in -> {
            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(MockedEndpointId.class)) {
                    MockedEndpointId mockedEndpointId = field.getAnnotation(MockedEndpointId.class);
                    Uri uri = field.getAnnotation(Uri.class);
                    if(mockedEndpointId == null || uri == null /*|| adviced.contains(mockedEndpointId.value())*/){
                        continue;
                    }

                    //adviced.add(mockedEndpointId.value());

                    if(field.getType().equals(ProducerTemplate.class)){
                        in.replaceFromWith(uri.value());
                    } else {
                        in.weaveById(mockedEndpointId.value())
                                .replace()
                                .to(uri.value());
                    }

                }
            }
        });

    }

    /**
     * Sends body object to endpoint
     */
    protected void sendToEndpoint(String endpoint, Object body) {
        try (ProducerTemplate producer = context.createProducerTemplate()) {
            producer.sendBody(endpoint, body);
        } catch (IOException e) {
            LOGGER.error("Exception occured: ", e);
        }
    }

    /**
     * Sends body object and headers to endpoint
     */
    protected void sendToEndpoint(CamelEndpoint endpoint, Object body, final Map<String, Object> headers) {


        try (ProducerTemplate producerTemplate = context.createProducerTemplate()) {
            producerTemplate.sendBodyAndHeaders(endpoint.endpointUri(), body, headers);
        } catch (IOException e) {
            LOGGER.error("Exception occured: ", e);
        }
    }
}
