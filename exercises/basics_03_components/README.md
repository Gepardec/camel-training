Components in Camel
===================

Prerequisites: Exercise Basic:02

Add ActiveMQ and Camel-ActiveMQ libraries to the projects pom.xml:

```
     <properties>
        <activemq-artemis-version>2.32.0</activemq-artemis-version>
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
			<artifactId>artemis-jakarta-client</artifactId>
			<version>${activemq-artemis-version}</version>
		</dependency>
		<dependency>
			<groupId>org.apache.activemq</groupId>
			<artifactId>artemis-server</artifactId>
			<version>${activemq-artemis-version}</version>
		</dependency>
```

Put broker and client beans into camel-context.xml:

```
...
  <!-- Start an embedded artemis server, configured with broker.xml -->
  <bean id="ActiveMQBroker" class="org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ"
   init-method="start" destroy-method="stop">
  </bean>

  <!-- lets configure the default ActiveMQ broker URL -->
  <bean id="activemq" class="org.apache.camel.component.jms.JmsComponent" depends-on="ActiveMQBroker">
    <property name="connectionFactory">
       <bean class="org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory">
        <constructor-arg value="tcp://localhost:61616"/>
      </bean>
    </property>
  </bean>
...
```

Copy broker.xml into the resources folder and fix the broker-url.

Import the test `JmsTest.java` into the application and make sure it is green.

Optional:
--------

Change the `activemq` in `from("activemq:wien")`  to `jms` and make the application run?



