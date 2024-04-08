My first Camel-Quarkus route
============================

Prerequisites: Maven, Java

Run
```
mvn io.quarkus:quarkus-maven-plugin:3.8.3:create \
    -DprojectGroupId=at.gepardec.camel.quarkus \
    -DprojectArtifactId=best \
    -Dextensions=camel-quarkus-log,camel-quarkus-core,camel-quarkus-file
``` 
Then run

``` 
cd best
mvn install
mvn quarkus:dev
``` 
Stop Quarkus in dev-mode, then build and run the quarkus app in non-dev mode.

Import the project into the IDE and add the following RouteBuilder:

```
package com.gepardec.training.camel.best;

import org.apache.camel.builder.RouteBuilder;

/**
 * Camel route definitions.
 */
public class FileRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("file:target/messages/in")
                .setBody(simple("Hello ${body}!"))
                .to("file:target/messages/out");
    }

}
```

What does the route do?
Test the route by copying a file to the in-folder.
