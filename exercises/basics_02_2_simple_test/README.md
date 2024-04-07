Test Routes in Spring Context
=============================

Prerequisites: Excercise 02_1

Step A)
Copy the Unit Test `SimpleTest.java` into your project and fix the test.


Discuss the following questions:
* What does the ProducerTemplate named `template` do?
* Can you access your route from your project from this test?

Step B)
* Include the route from `MyRouteBuilder` into the test. Hint: Use `context().addRoutes(...)` in `configure()`
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
