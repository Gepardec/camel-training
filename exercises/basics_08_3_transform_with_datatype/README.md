Transform with DataTypeAware Messages
=====================================

1. Write a transformer that transforms `OrderToProducer` messages in JSON format to java objects.
2. Write a route with `inputType(OrderToProducer.class)` and send `msgIn = ExampleFiles.ORDER_TO_PRODUCER_EGGS_JSON`
to the route. The message should have the DataType `"json:OrderToProducer"`

