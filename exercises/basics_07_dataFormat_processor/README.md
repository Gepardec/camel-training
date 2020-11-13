Transforming and Processing Data
================================

Prerequisites: Exercise 06.1

Transform the order in Jason-Format to a Java object, then change the partner-ID to another value.
Finally transform the Java object back to a File.

Use `String orderIn = "{\"partnerId\": 1, \"items\": [{ \"code\": 1, \"amount\": 110 }]}";`

Hints
-----

Add the following dependency to pom.xml:

```
   <dependency>
     <groupId>org.apache.camel</groupId>
     <artifactId>camel-jackson</artifactId>
   </dependency>

    <!-- domain -->
    <dependency>
      <groupId>com.gepardec.training.camel</groupId>
      <artifactId>commons</artifactId>
      <version>${project.version}</version>
    </dependency>
```

Marshal with help of Jackson:

```
    JacksonDataFormat orderFormat = new JacksonDataFormat(Order.class);
...
    .unmarshal(orderFormat)
...
```
Use the classes `Order*.java` for Json processing.
Add a processor:

```
       .process(new Processor() {
                      
                  @Override
                  public void process(Exchange exchange) throws Exception {
                          exchange.getIn().getBody(Order.class).setPartnerId(34);
                  }
          })
```

How does a Exchange lool like?
