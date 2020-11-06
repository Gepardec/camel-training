package com.gepardec.training.camel.commons.test.routetest;


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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
        Set<String> routeIds = new HashSet<>();
        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(RouteId.class)) {
                routeIds.add(field.getAnnotation(RouteId.class).value());
            }
        }

        for (String routeId : routeIds) {
            adviceRoute(routeId);
        }

    }

    private void adviceRoute(String routeId) throws Exception {
        AdviceWithRouteBuilder.adviceWith(context, routeId, in -> {
            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(MockableEndpoint.class)) {
                    MockableEndpoint endpoint = field.getAnnotation(MockableEndpoint.class);
                    Uri uri = field.getAnnotation(Uri.class);
                    RouteId providedRouteId = field.getAnnotation(RouteId.class);
                    if(endpoint == null || uri == null || providedRouteId == null || !providedRouteId.value().equals(routeId)){
                        continue;
                    }

                    if(field.getType().equals(ProducerTemplate.class)){
                        in.replaceFromWith(uri.value());
                    } else if(!endpoint.value().isEmpty()) {
                        in.weaveByToUri(endpoint.value())
                                .replace()
                                .to(uri.value());
                    }
                    else if(!endpoint.id().isEmpty()) {
                        in.weaveById(endpoint.id())
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
}
