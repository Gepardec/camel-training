Test Routes in Spring Context
=============================

Prerequisites: Excercise 02_2

Step A)
Include the Spring Context from your Project into the Test-Context and test your Java route from the project:

*) Use `org.apache.camel.test.spring.CamelSpringTestSupport` instead of `CamelTestSupport`
*) Include the Spring context by overriding

```
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

...

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
    }
```

*) Remove the `context().addRoutes(...)` from the previous example.
*) Test the route.
*) Inject the ProducerTemplate and MockEndpoint.
