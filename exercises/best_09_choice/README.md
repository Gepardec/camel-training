Adding Choice to SplitterRoute
================================

Prerequisites: Exercise 08

Add a choice section to the splitter route.

Use the OrderItem.code to send the body to the correct queue. 

```
.when(hasItemCode(OrderItem.EGG))
```

How a test could look like.(not complete)
```    @Inject
       @Uri("mock:milk_result")
       @MockedEndpointId(MilkOrderRouteBuilder.ENTRY_SEDA_ENDOINT_ID)
       private MockEndpoint milkResult;
   
       @Inject
       @Uri("mock:result")
       private MockEndpoint mockResult;

    @Test
    public void correctInput_CorrectOutputToMilk(@Uri(SplitterRouteBuilder.ENTRY_SEDA_ENDOINT_URI) ProducerTemplate producer) throws InterruptedException {
        milkResult.expectedMessageCount(1);

        // Then
        producer.sendBody(order);
        milkResult.assertIsSatisfied();
        final Exchange exchange = milkResult.getExchanges().get(0);
        assertThat(exchange).isNotNull();

        OrderToProducer order = exchange.getIn().getBody(OrderToProducer.class);
        assertThat(order).isNotNull();
        assertThat(order.getAmount()).isEqualTo(130);
        assertThat(order.getPartnerId()).isEqualTo(1L);
    }

```

