package com.gepardec.trainings.camel.best;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;


public class SimpleTest extends CamelTestSupport {

	
    @Test
    public void test_simple_with_body() throws InterruptedException {
        String msgIn = "Gepardec";
        String msgExpected = "Willkommen bei Gepardec";

        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:result", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(msgExpected);
        
        
        template.sendBody("direct:start", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("direct:start")
                .log("Got message: ${body}")
                .setBody(simple("my ${body}"))
               .to("mock:result");
            }
        };
    } 

	public File readResourceFile( String path) throws IOException {
		 ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		 try {
			return new File(classloader.getResource(path).toURI());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String readResourceFileToString( String path){
		 try {
			return new String(Files.readAllBytes(readResourceFile(path).toPath()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
