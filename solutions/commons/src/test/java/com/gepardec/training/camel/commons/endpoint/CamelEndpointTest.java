package com.gepardec.training.camel.commons.endpoint;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CamelEndpointTest {

    public static final String ID_TEST = "test";

    @Test
    void emptyEndpointUser_ExceptionThrown() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new CamelEndpoint("", ""));
    }

    @Test
    void sedaEndpoint_SameTestAndRouteEndpoint() {
        String uri = "seda:test";
        CamelEndpoint endpoint = new CamelEndpoint(uri, ID_TEST);

        CamelEndpoint.setTestMode(true);
        String testEndpoint = endpoint.endpointUri();

        CamelEndpoint.setTestMode(false);
        String routeEndpoint = endpoint.endpointUri();

        assertThat(testEndpoint).isEqualTo(routeEndpoint).isEqualTo(uri);
    }

    @Test
    void directEndpoint_SameTestAndRouteEndpoint() {
        String uri = "direct:test";
        CamelEndpoint endpoint = new CamelEndpoint(uri, ID_TEST);

        CamelEndpoint.setTestMode(true);
        String testEndpoint = endpoint.endpointUri();

        CamelEndpoint.setTestMode(false);
        String routeEndpoint = endpoint.endpointUri();

        assertThat(testEndpoint).isEqualTo(routeEndpoint).isEqualTo(uri);
    }

    @Test
    void httpEndpoint_DirectTestEndpoint() {
        String uri = "http:test";
        CamelEndpoint endpoint = new CamelEndpoint(uri, ID_TEST);

        CamelEndpoint.setTestMode(true);
        String testEndpoint = endpoint.endpointUri();

        CamelEndpoint.setTestMode(false);
        String routeEndpoint = endpoint.endpointUri();

        assertThat(testEndpoint).isEqualTo("direct:test");
        assertThat(routeEndpoint).isEqualTo("http:test");
    }

    @Test
    void emailEndpoint_SedaTestEndpoint() {
        String uri = "smtps://test?username=usr&password=pwd";
        CamelEndpoint endpoint = new CamelEndpoint(uri, ID_TEST);

        CamelEndpoint.setTestMode(true);
        String testEndpoint = endpoint.endpointUri();

        CamelEndpoint.setTestMode(false);
        String routeEndpoint = endpoint.endpointUri();

        assertThat(testEndpoint).isEqualTo("seda://test_username_usr_password_pwd");
        assertThat(routeEndpoint).isEqualTo("smtps://test?username=usr&password=pwd");
    }
}