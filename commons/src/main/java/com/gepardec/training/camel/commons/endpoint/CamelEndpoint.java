package com.gepardec.training.camel.commons.endpoint;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Wrapper over the endpoints in order to make them testable
 */
public class CamelEndpoint {

    private static final Set<String> SYNC_ENDPOINTS = new HashSet<>(Arrays.asList("http",
            "https",
            "file"));
    private static final Set<String> ASYNC_ENDPOINTS = new HashSet<>(Arrays.asList("smtp",
            "pop3",
            "imap",
            "smtps",
            "pop3s",
            "imaps",
            "jms"));
    public static final String ENDPOINT_TYPE_DIRECT = "direct";
    public static final String ENDPOINT_TYPE_SEDA = "seda";
    public static final String SYSPROP_TEST_MODE_ENABLED = "test.mode.enabled";

    private String uri;
    private String id;

    public CamelEndpoint(String uri, String id) {
        if(StringUtils.isEmpty(uri) || StringUtils.isEmpty(id)){
            throw new IllegalArgumentException("URI and ID must not be empty");
        }
        this.uri = uri;
        this.id = id;
    }

    public static void setTestMode(boolean testModeEnabled){
        if(testModeEnabled){
            System.setProperty(SYSPROP_TEST_MODE_ENABLED, "true");
        } else {
            System.clearProperty(SYSPROP_TEST_MODE_ENABLED);
        }
    }

    /**
     * Returns the endpoint uri to be used in a route, if the test mode is not enabled, otherwise endpoint for testing
     */
    public String endpointUri(){
        if (Boolean.getBoolean(SYSPROP_TEST_MODE_ENABLED)){
            return testEndpoint();
        } else {
            return normalEndpoint();
        }
    }

    /**
     * Returns configured endpoint id
     */
    public String getId(){
        return id;
    }

    /**
     * Returns the endpoint uri to be used in a route
     */
    private String normalEndpoint(){
        return uri;
    }

    /**
     * Returns the endpoint uri to be used in a test
     */
    private String testEndpoint(){
        int index = uri.indexOf(':');
        if(index == -1 || uri.startsWith(ENDPOINT_TYPE_DIRECT) || uri.startsWith(ENDPOINT_TYPE_SEDA)){
            return uri;
        }

        String endpointType = uri.substring(0, index);
        if(SYNC_ENDPOINTS.contains(endpointType)){
            return prepareEndpointForTest(uri, endpointType, ENDPOINT_TYPE_DIRECT);
        }

        if(ASYNC_ENDPOINTS.contains(endpointType)){
            return prepareEndpointForTest(uri, endpointType, ENDPOINT_TYPE_SEDA);
        }

        return uri;
    }

    private String prepareEndpointForTest(String uri, String sourceEndpointType, String destinationEndpointType) {
        String result = uri.replace(sourceEndpointType, destinationEndpointType);
        result = result.replaceAll("[?&=]", "_");
        return result;
    }
}
