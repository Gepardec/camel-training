package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.test.TestBase;
import com.gepardec.training.camel.commons.test.integrationtest.RestServiceTestSupport;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class EntryRouteBuilderIT extends TestBase {

    private static final String WRONG_JSON_FILE_PATH = "json/order_wrong_format.json";
    private static final String JSON_FILE_PATH = "json/order.json";

    @Test
    public void wrongInputJson_ValidationFails() throws IOException {
        String wrongJson = getFileAsString(WRONG_JSON_FILE_PATH);
        RestServiceTestSupport.callPost("", wrongJson, 400);
    }

    @Test
    public void correctInputJson_CorrectJavaObjectIsCreated() throws IOException {
        String json = getFileAsString(JSON_FILE_PATH);
        RestServiceTestSupport.callPost("", json, 202);
    }

}