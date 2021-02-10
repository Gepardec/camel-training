CDI Unit-Tests with Camel
===========================

Prerequisites: Exercise 05

Create a route that takes Order-Files from `src/orders` and sends it to `target/orders/processed`. 
Test this route with help of a unit test that uses `CamelTestSupport` as base class.

Hints
-----

Add the following dependency to pom.xml:

```
   <dependency>
       <groupId>org.apache.camel</groupId>
       <artifactId>camel-test-cdi</artifactId>
       <scope>test</scope>
   </dependency>
```

Copy the route `MyRoutes.java` into your project.

Add a unit test with the following test and route builder:

```
    @Test
    public void when_order_in_orders_message_is_in_processed() throws InterruptedException {
        String orderIn = "{\"partnerId\":1,\"items\":[{\"code\":1,\"amount\":110}]}";
        String orderExpected = "{\"partnerId\":1,\"items\":[{\"code\":1,\"amount\":110}]}";

        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(orderExpected);
               
        template.sendBody("direct:start", orderIn);
        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("direct:start")
                .log("Send to file order: ${body}")
                .to(MyRoutes.URL_FILE_ORDERS_IN);
                               
                from(MyRoutes.URL_FILE_ORDERS_OUT)
                .to(resultEndpoint);
                
            }
        };
```

Test the test.

Simplify the test by using Advice and weave:

```
	void advice(@Observes CamelContextStartedEvent event, ModelCamelContext context) throws Exception {
		AdviceWith.adviceWith(context.getRouteDefinition(MyRoutes.DIRECT_FROM_ROUTE), context, new AdviceWithRouteBuilder() {
			@Override
			public void configure() {
				weaveByToUri(MyRoutes.URL_FILE_ORDERS_OUT).replace().to(resultMock);
			}
		});
	}

	@Test
	public void when_order_in_orders_message_is_in_processed() throws InterruptedException {
        String orderIn = "{\"partnerId\":1,\"items\":[{\"code\":1,\"amount\":110}]}";
        String orderExpected = "{\"partnerId\":1,\"items\":[{\"code\":1,\"amount\":110}]}";

		resultMock.expectedMessageCount(1);
		resultMock.expectedBodiesReceived(orderExpected);

		template.sendBody(fromEndpoint, orderIn);
		resultMock.assertIsSatisfied();
	}

```

Optional: Try also to replace the from-endpoint ("file:src/orders") from the main route in the test by a direct endpoint.
