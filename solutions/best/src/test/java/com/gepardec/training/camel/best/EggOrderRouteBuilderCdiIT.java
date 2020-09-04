package com.gepardec.training.camel.best;

import com.gepardec.training.camel.best.config.Endpoints;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ServiceStatus;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.cdi.Uri;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.cdi.Beans;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(CamelCdiRunner.class)
@Beans(classes = EggOrderRouteBuilder.class)
public class EggOrderRouteBuilderCdiIT {

    private static final String MILK_JSON_FILE_PATH = "json/order_milk.json";
    private static final String EGGS_JSON_FILE_PATH = "json/order_eggs.json";

    @Inject
    CamelContext context;

    @Inject
    @Uri("mock:result")
    private MockEndpoint result;


    @Before
    public void setUp() throws Exception {

        //Camel v2 way
//        ModelCamelContext mcc = context.adapt(ModelCamelContext.class);
//        mcc.adviceWith(mcc.getRouteDefinition("EggOrderRoute"), new AdviceWithRouteBuilder() {
//            @Override
//            public void configure() throws Exception {
//                interceptSendToEndpoint(Endpoints.EGG_ORDER_JMS_ENDPOINT.endpointUri())
//                        .skipSendToOriginalEndpoint()
//                        .to("mock:result");
//            }
//        });

        //Camel v3 way
        AdviceWithRouteBuilder.adviceWith(context, "EggOrderRoute", in -> {
            in.interceptSendToEndpoint(Endpoints.EGG_ORDER_JMS_ENDPOINT.endpointUri())
                    .skipSendToOriginalEndpoint()
                    .to("mock:result");
        });
    }

    @Test
    public void testCamelContextStarted() {
        assertThat(context.getStatus()).isEqualTo(ServiceStatus.Started);
    }

    @Test
    public void testMessageBeforeJms() throws InterruptedException {
        result.expectedMessageCount(1);


        result.expects(() -> {
            final Exchange exchange = result.getExchanges().stream().findFirst().orElse(null);
            Assert.assertNotNull(exchange);
        });

        // Then
        result.assertIsSatisfied();
    }

}