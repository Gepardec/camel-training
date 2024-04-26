Transforming and Processing Data
================================

Prerequisites: Exercise 06.1

Transform a order in JSON-Format to a Java object. 
The order can be processed as Java object.
Finally transform the Java object back to a JSON-string.

Steps
-----

Add the following dependency to pom.xml:

```
   <dependency>
     <groupId>org.apache.camel.quarkus</groupId>
     <artifactId>camel-quarkus-jackson</artifactId>
   </dependency>

    <!-- domain -->
    <dependency>
      <groupId>com.gepardec.training.camel</groupId>
      <artifactId>commons</artifactId>
      <version>1.0.0-SNAPSHOT</version>
    </dependency>
```

Copy the `MarshalTest.java` to your project.
Change the lines marked with TODO

Unmarshal to type `OrderToProducer.class` and  marshal with help of JacksonDataFormat:

