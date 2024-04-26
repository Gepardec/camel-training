package com.gepardec.trainings.camel.best;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringTestSupport;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class JmsTest extends CamelSpringTestSupport {

    @Produce("direct:startJmsTest")
    private ProducerTemplate template;

    @EndpointInject("mock:resultJmsTest")
    private MockEndpoint resultEndpoint;


    @Test
    public void test_simple_with_body() throws InterruptedException, Exception {

        String msgIn = readResourceFileToString("wienMessage.xml");
        String msgExpected = msgIn;

        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(msgExpected);


        template.sendBody(msgIn);
        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("direct:startJmsTest")
                .log("Got message: ${body}")
                .to("jms:testQueue");
                
                from("jms:testQueue")
                .to(resultEndpoint);
                
            }
        };
    }

	@Override
	protected AbstractApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
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
	
	public String readResourceFileToString( String path) throws IOException {
		 try {
			return new String(Files.readAllBytes(readResourceFile(path).toPath()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
