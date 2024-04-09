Using XML Camel Context with Quarkus
====================================

Prerequisites: Exercise 04

Add a route that adds the content from a bean (Hello.java) to the message.

Steps
-----

Add the following dependencies to pom.xml:

```
     <dependency>
       <groupId>org.apache.camel.quarkus</groupId>
       <artifactId>camel-quarkus-xml-io-dsl</artifactId>
     </dependency>
     <dependency>
       <groupId>org.apache.camel.quarkus</groupId>
       <artifactId>camel-quarkus-bean</artifactId>
     </dependency>
```

Copy the Hello.java to the application.

Add the camel.xml to the src/main/resources directory
Register the camel.xml in application.properties (camel.main.routes-include-pattern)

Test the application

What does the 
```
		<setBody>
			<simple>"${body} add from bean: ${bean:messageString}!</simple>
		</setBody>
```
do?
