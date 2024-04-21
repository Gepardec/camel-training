Configure a REST Interface
==========================

Provide a REST interface for the application and test it with `curl`.

Steps
-----

Add the following dependencies to the `pom.xml`:
For REST:
```
	<dependency>
		<groupId>org.apache.camel.quarkus</groupId>
		<artifactId>camel-quarkus-rest</artifactId>
	</dependency>
	<dependency>
		<groupId>jakarta.ws.rs</groupId>
		<artifactId>jakarta.ws.rs-api</artifactId>
	</dependency>
```
and (if not present) for `seda` and `direct` entpoints
```
	<dependency>
		<groupId>org.apache.camel.quarkus</groupId>
		<artifactId>camel-quarkus-direct</artifactId>
	</dependency>
	<dependency>
		<groupId>org.apache.camel.quarkus</groupId>
		<artifactId>camel-quarkus-seda</artifactId>
	</dependency>
```

Create a RouteBuilder:

```
public final class EntryRouteBuilder extends RouteBuilder {

    public static final String ORDER_FILE_ENDOINT_URI = "file:target/messages/order";
    public static final String ENTRY_DIRECT_ENDOINT_URI = "direct://order_entry";
    public static final String ENTRY_REST_ENDOINT_ROUTE_ID = "rest://order_entry";

    @Override
    public void configure() {
    // TODO Configure here
    }
}
```

Configure the component with `restConfiguration().bindingMode(RestBindingMode.json);`

Configure a post-endpoint at the url `/best/` that accepts orders:

```
        rest("/best/")
        	.post().routeId(ENTRY_REST_ENDOINT_ROUTE_ID)
        	.type(Order.class)
        	.to(ENTRY_DIRECT_ENDOINT_URI);
```

Write the message to a file (`ORDER_FILE_ENDOINT_URI`) and return the following header and body:

```
        	.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(Response.Status.ACCEPTED.getStatusCode()))
        	.setBody(constant("OK"));
```

Notice that you need to `.marshal().json(true)` the order before writing it to a file.

Start Quarkus with `mvn quarkus:dev` and call the REST-interface with:

```
curl -v --header "Content-Type: application/json" http://localhost:8080/best -d '{ "partnerId": 1, "items": [{ "code": 1, "amount": 110 }]}'
```

The result should be:
```
...
< HTTP/1.1 202 Accepted
< Accept: */*
< User-Agent: curl/7.78.0-DEV
< transfer-encoding: chunked
< Content-Type: application/json
< 
* Connection #0 to host localhost left intact
"OK"
```

In `target/messages/order` should be files

Alternatively you may invoke curl with a file:
```
curl -v -X POST --header "Content-Type: application/json" http://localhost:8080/best --data-binary @solutions/commons/src/main/resources/files/json/order.json
```
