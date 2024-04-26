Test Routes in Spring Context
=============================

Prerequisites: Excercise 02_2

Step A)
------
Include the Spring Context from your Project into the Test-Context and test your Java route from the project:

* Use `org.apache.camel.test.spring.CamelSpringTestSupport` instead of `CamelTestSupport`
* Include the Spring context by overriding

```
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

...

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
    }
```

* Use the following route for testing:

```
        from("direct:start")
        .log("Got message: ${body}")
        .to("file:target/messages/others");
        
        from("file:target/messages/somewhere?noop=true")
        .to("mock:result");
```

* Put the file with the 'Wien'-message into the `test/resources` folder and read the content with `readResourceFileToString()`.
* Test that the route from MyRouteBuilder() works properly.


Step B)
-------

* Inject the ProducerTemplate and MockEndpoint.

You should clean your message directory before you test:


```
	@BeforeEach
    public void cleanMessages() throws Exception {
        FileUtils.deleteDirectory(new File("target/messages"));
    }
```

Hint: Add Maven dependency

```
    <dependency>
	    <groupId>commons-io</groupId>
	    <artifactId>commons-io</artifactId>
	    <version>2.15.1</version>
        <scope>test</scope>
    </dependency>
```
