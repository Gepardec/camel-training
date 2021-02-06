package at.gepardec.trainings.camel;

import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {

    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {

        from("file:target/messages/others?noop=true")
        .to("direct:infile");
        
        from("direct:infile")
        .to("file:target/messages/somewhere");
    }

}
