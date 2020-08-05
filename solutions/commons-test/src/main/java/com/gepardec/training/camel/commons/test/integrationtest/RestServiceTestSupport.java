package com.gepardec.training.camel.commons.test.integrationtest;

import io.restassured.response.Response;

import java.util.Optional;

import static io.restassured.RestAssured.given;

public class RestServiceTestSupport {

    public static final String BEST_URL = "/best";

    private static String getServiceUrl(){
        String baseUrl = System.getProperty("it.base.url", "http://localhost:8080");
        return baseUrl + BEST_URL;

    }

    public static String callPost(String resourceUrl, String body, int expectedStatus){
        String url = getServiceUrl() + Optional.ofNullable(resourceUrl).orElse("");
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(body)
                .when()
                .post(url)
                .then()
                .statusCode(expectedStatus)
                .and()
                .extract()
                .response();

        return response.asString();
    }
}
