package com.gepardec.trainings.camel.best;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import at.gepardec.trainings.camel.MyRouteBuilder;


public class SimpleTest2 extends CamelTestSupport {


	
    @Test
    public void test_simple_with_body_no_inject() throws InterruptedException {
        String msgIn = readResourceFileToString("wienMessage.xml");
        String msgExpected = msgIn;

        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:result", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(msgExpected);


        template.sendBody("direct:start", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() throws Exception {
            	
            	context().addRoutes(new MyRouteBuilder());
 
                from("direct:start")
                .log("Got message: ${body}")
               .to("file:target/messages/others");
                
                from("file:target/messages/somewhere?noop=true")
                .to("mock:result");
            }
        };
    }
	
	@BeforeEach
    public void cleanMessages() throws Exception {
        FileUtils.deleteDirectory(new File("target/messages"));
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
