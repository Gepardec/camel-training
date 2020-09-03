Components in Camel
===================

Prerequisites: Exercise Basic:02

Add ActiveMQ and Camel-ActiveMQ libraries to the projects pom.xml:

```
     <properties>
        <activemq-version>5.15.13</activemq-version>
     </properties>
```
and dependencies:
```
 
        <dependency>
            <groupId>org.apache.camel</groupId>
            <artifactId>camel-activemq</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.activemq</groupId>
            <artifactId>activemq-broker</artifactId>
            <version>${activemq-version}</version>
        </dependency>
               <dependency>
                   <groupId>org.apache.activemq</groupId>
                   <artifactId>activemq-spring</artifactId>
                   <version>${activemq-version}</version>
               </dependency>
        <dependency>
            <groupId>org.apache.activemq</groupId>
            <artifactId>activemq-client</artifactId>
            <version>${activemq-version}</version>
            <!-- lets use JMS 2.0 api but camel-jms still works with ActiveMQ 5.x that is JMS 1.1 only -->
            <exclusions>
                <exclusion>
                    <groupId>org.apache.geronimo.specs</groupId>
                    <artifactId>geronimo-jms_1.1_spec</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
```

Change the namespaces in camel-context.xml and add the following:

```
...
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://camel.apache.org/schema/spring http://camel.apache.org/schema/spring/camel-spring.xsd
        http://activemq.apache.org/schema/core http://activemq.apache.org/schema/core/activemq-core.xsd
        ">
        
	  <!-- This creates an embedded ActiveMQ Broker -->
	  <broker xmlns="http://activemq.apache.org/schema/core"
		    brokerName="myBroker" id="broker" persistent="false"
		    useJmx="true" useShutdownHook="false" >
		    <transportConnectors>
		        <transportConnector uri="tcp://0.0.0.0:61616"/>
		    </transportConnectors>
	  </broker>
	  
	  
	  <bean class="org.apache.camel.component.activemq.ActiveMQComponent"
	      depends-on="broker" id="activemq">
	      <property name="brokerURL" value="tcp://localhost:61616"/>
	  </bean>
...
```

Add the following routes:

```
        from("file:target/messages/at?noop=true")
        .to("activemq:wien");       
 
        from("activemq:wien")
        .to("log:read-wien")
        .to("file:target/messages/wien");       

```

Test the application.
   * What does the application do?
   * Hwo many processes are running?
   * How many messages end up in target/messages/wien ?
   * Can you cahnge the "activemq" in 'from("activemq:wien")' and make the application run?



