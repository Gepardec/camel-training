package com.gepardec.trainings.camel.best;

import org.apache.camel.BindToRegistry;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.cdi.Beans;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(CamelCdiRunner.class)
@Beans(alternatives = OtherTestBean.class)
public class AlternativeBeanProcessingTest extends CamelTestSupport {

    @Test
    public void when_alternative_get_alternative_world() throws InterruptedException {
        String orderIn = "{\"partnerId\":1,\"items\":[{\"code\":1,\"amount\":110}]}";
        String orderExpected = "{\"partnerId\":39,\"items\":[{\"code\":1,\"amount\":110}]}";

        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:result", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(orderExpected);
                
        template.sendBody("direct:start", orderIn);
        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
        		System.out.println("Hello Registry:" + context().getRegistry().findByType(Object.class).toArray());
          
               from("direct:start")
                .log("Got order: ${body}")
                .to(MyRoutes.URL_FILE_ORDERS_IN);
               
               from(MyRoutes.URL_FILE_ORDERS_OUT)
                .log("End with: ${body}")
                .to("mock:result");
               
            }
        };
    } 
}