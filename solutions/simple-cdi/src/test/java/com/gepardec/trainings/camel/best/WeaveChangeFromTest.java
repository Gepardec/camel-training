package com.gepardec.trainings.camel.best;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.camel.Endpoint;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.cdi.Uri;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.spi.CamelEvent.CamelContextStartedEvent;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CamelCdiRunner.class)
public class WeaveChangeFromTest extends CamelTestSupport {

	@Inject
	@Uri("direct:fromMock")
	private Endpoint fromMock;

	@Inject
    @Uri("mock:result")
    MockEndpoint resultMock;


	void advice(@Observes CamelContextStartedEvent event, ModelCamelContext context) throws Exception {
		AdviceWith.adviceWith(context.getRouteDefinition(MyRoutes.DIRECT_BEAN_OTHER), context, new AdviceWithRouteBuilder() {
			@Override
			public void configure() {
				weaveByToUri(MyRoutes.URL_FILE_ORDERS_OUT).replace().to(resultMock);
			}
		});
		AdviceWith.adviceWith(context.getRouteDefinition(MyRoutes.URL_FILE_ORDERS_IN), context, new AdviceWithRouteBuilder() {
			@Override
			public void configure() {
				replaceFromWith(fromMock);
			}
		});
	}

	@Test
	public void when_order_in_orders_message_is_in_processed() throws InterruptedException {
        String orderIn = "{\"partnerId\":1,\"items\":[{\"code\":1,\"amount\":110}]}";
        String orderExpected = "{\"partnerId\":34,\"items\":[{\"code\":1,\"amount\":110}]}";

		resultMock.expectedMessageCount(1);
		resultMock.expectedBodiesReceived(orderExpected);

		template.sendBody(fromMock, orderIn);
		resultMock.assertIsSatisfied();
	}

}