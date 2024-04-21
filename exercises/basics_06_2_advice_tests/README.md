Using AdviceWith to test routes
===============================

Prerequisites: Exercise 06.1

Use `AdviceWith` to replace the from- and to-endpoints from the File route (Exercise 06.1)
such that a test with the following routes succeed:

```
                from("direct:start" + TEST_NAME + "1") // you may adapt this line
                	.log("Got message: ${body}")
                	.to("direct:FileRouteTestFrom");
                
                from("direct:FileRouteTestTo")
                	.to(resultEndpoint);
```

Steps
-----

Add `@TestInstance(Lifecycle.PER_CLASS)` and `@TestProfile` annotations to the test class.

Add a `routeId(...)` to the File route

In a `@BeforeAll` method create an advice and replace the from endpoint with "direct:FileRouteTestFrom".
Also replace the to-endpoint with "direct:FileRouteTestTo".

Hint: you may just use the variable `context` from `CamelQuarkusTestSupport` as CamelContext.

Make sure the test is green.

