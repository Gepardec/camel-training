package com.gepardec.training.camel.best;

import org.apache.camel.BindToRegistry;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import com.gepardec.training.camel.commons.config.ConfigurationProducer;
import com.gepardec.training.camel.commons.test.TestBase;
import com.gepardec.training.camel.commons.test.integrationtest.CamelIntegrationTest;

public class EggOrderIT extends CamelTestSupport {

    private static final String MILK_JSON_FILE_PATH = "testmessages/order_milk.json";
    private static final String EGGS_JSON_FILE_PATH = "testmessages/order_eggs.json";
    private static final String EXPECTED_EGGS_XML_FILE_PATH = "testmessages/order_eggs.xml";

    @BindToRegistry
    public JmsComponent myjms() {
    	return new ConfigurationProducer().jms();
    }
    
    @Test
    public void egg_json_is_transferred_to_queue() throws Exception {

    	String orderIn = TestBase.getFileAsString(EGGS_JSON_FILE_PATH);
        String orderExpected = TestBase.getFileAsString(EXPECTED_EGGS_XML_FILE_PATH);

        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:result", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(orderExpected);
             
        template.sendBody("direct:start", orderIn);
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void milk_is_not_transferred_to_queue() throws Exception {
        String orderIn = CamelIntegrationTest.getFileAsString(MILK_JSON_FILE_PATH);

        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:result", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(0);
             
        template.sendBody("direct:start", orderIn);
        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("direct:start")
                .log("Send to rest: order: ${body}")
                .to("rest:post:best?host=localhost:8080");
                               
                from("myjms://queue:eggs")
                .to("mock:result");
                
            }
        };
    } 

}