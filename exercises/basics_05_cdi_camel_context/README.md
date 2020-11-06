Using XML Camel Context with CDI
================================

Prerequisites: Exercise 04

Add a route that logs a timer in a camel-context.xml

Hints
-----

Add the following dependency to pom.xml:

```
    <dependency>
        <groupId>org.apache.camel</groupId>
        <artifactId>camel-core-xml</artifactId>
        <scope>runtime</scope>
    </dependency>
```

Add ImportResource annotation to the RouteBuilder class:

```
 @ImportResource("camel-context.xml")
 public class MyRoutes extends RouteBuilder {
```

Add `camel-context.xml` to src/main/resources

```
<?xml version="1.0" encoding="UTF-8"?>

<camelContext id="cdi-camel-xml"
    xmlns="http://camel.apache.org/schema/spring"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://camel.apache.org/schema/spring               http://camel.apache.org/schema/spring/camel-spring.xsd">

    <route id="timer_route1">
        <description>Route that shows running camel</description>
        <from uri="timer:forLogging?period=5000"/>
        <to uri="log:vom_xml"/>
    </route>
</camelContext>
```
