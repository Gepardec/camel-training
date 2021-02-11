Java DSL Example
===================

Prerequisites: Maven, Java

Add a RouteBuilder class to the project:

```
package at.gepardec.trainings.camel;

import org.apache.camel.builder.RouteBuilder;

public class MyRouteBuilder extends RouteBuilder {

    @Override
    public void configure() {

        from("file:target/messages/others?noop=true")
        .to("file:target/messages/somewhere");
    }

}
```

In order to add that route to the camel context, add a <package> element to the Spring configuration:

```
...
    <camelContext id="camelContext-36a8d3ba-2a31-44cc-ae01-1e9a04f7b88d" xmlns="http://camel.apache.org/schema/spring">
        <package>at.gepardec.trainings.camel</package>
...
```

Test the routes manually
