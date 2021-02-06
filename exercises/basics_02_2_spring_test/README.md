Test Routes in Spring Context
=============================

Prerequisites: Excercise 02_1

Step A)
Copy the Unit Test `SimpleTest.java` into your project and fix the test.

Discuss the followong Questions:
* What does the ProducerTemplate named `template` do?
* What does the method `simple(...)` in the route do?
* Can you access your route from your project from this test?

Step B)
Include the Spring Context from your Project into the Test-Context and test your Java route from the project:

1) Use `org.apache.camel.test.spring.CamelSpringTestSupport` instead of `CamelTestSupport`
2) Include the Spring context by overriding

```
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

...

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
    }
```

You might want to clean your message directory before you test:

```
    @Override
    @Before
    public void setUp() throws Exception {
        deleteDirectory("target/messages");
        super.setUp();
    }
```
