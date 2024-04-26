package at.gepardec.trainings.camel;

import org.apache.camel.builder.RouteBuilder;

public class MyRouteBuilder extends RouteBuilder {

    @Override
    public void configure() {

        from("file:target/messages/others?noop=true")
        .to("file:target/messages/somewhere");
        
        from("file:target/messages/at?noop=true")
        .to("jms:wien");

        from("jms:wien")
        .to("log:read-wien")
        .to("file:target/messages/wien");

    }

}
