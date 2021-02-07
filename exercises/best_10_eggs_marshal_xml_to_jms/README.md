Creating Marshalling Route to JMS
=================================

We want to send orders for eggs in XML format to our organic chicken farmer. He uses an ActiveMQ messaging system.
Create a route with an SEDA Endpoint (eggEndpoint), marshal the message to XML and send it to the `eggs` queue from ActiveMQ.
Test with an integration test.

Hints:

Use Jaxb as data format.
```
  JaxbDataFormat xml = new JaxbDataFormat();

```
There is already a jms client component configured in `ConfigurationProducer` (find out where exactly).
With that you can use the following URI:
```
    public static final String OUTPUT_JMS_ENDPOINT_URI = "jms://queue:eggs";
```

You can use the following route:
```
    from(entryEndpoint).routeId(ENTRY_SEDA_ENDOINT_URI)
            .filter(body().isInstanceOf(OrderToProducer.class))
            // convert here to XML
            .log("Sending ${body} to " + OUTPUT_JMS_ENDPOINT_URI)
            // set the exchange pattern to inOnly
            .to(jmsEndpoint).id(OUTPUT_JMS_ENDPOINT_ID);
```

For the integration test you may use the provided EggOrderIT.java and files in testmessages.
Discuss the following items in the test:

   * @BindToRegistry
   * REST-Component
