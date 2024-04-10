CDI Unit-Tests with Camel
===========================

Prerequisites: Exercise 05

Create a route that takes files from `target/messages/in` and sends it to `target/messages/in`. 
Test this route with help of a unit test that uses `CamelQuarkusTestSupport` as base class.

Steps
-----

Add the following dependency to pom.xml:

```
    <dependency>
        <groupId>org.apache.camel.quarkus</groupId>
        <artifactId>camel-quarkus-junit5</artifactId>
        <scope>test</scope>
    </dependency>

```

Copy the route `FileRoute.java` into your project in case you dont already have it.

Add a unit test with the following test and route builder:

```
@QuarkusTest
public class FileRouteTest extends CamelQuarkusTestSupport {

    @Produce("direct:start")
    private ProducerTemplate startProducer;

    @EndpointInject("mock:result")
    private MockEndpoint resultEndpoint;

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() throws Exception {

                from("direct:start")
                        .log("Got message: ${body}")
                        .to("file:target/messages/in");

                from("file:target/messages/out")
                        .to("mock:result");
            }
        };
    }

    @Test
    public void test_message_goes_from_in_to_out() throws InterruptedException {
        String msgIn = "Camel";
        String msgExpected = "Hallo Camel!";

        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody("direct:start", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

}

```

Test the test and fix the Bug.
