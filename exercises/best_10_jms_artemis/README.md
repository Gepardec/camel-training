Send Message to Artemis JMS Broker
==================================

Configure an Artemis broker in Quarkus an use a Camle route to send data to a queue.

Steps
-----

Start the Artemis JMS broker:

```
docker run -d --rm --name activemq-artemis -p 8161:8161 -p 61616:61616 -p 5672:5672 -e ARTEMIS_USERNAME=camel -e ARTEMIS_PASSWORD=camel vromero/activemq-artemis:2.9.0-alpine
```

Add the Maven dependency to dependencyManagement section:

```
		<dependency>
	        <groupId>io.quarkiverse.artemis</groupId>
	        <artifactId>quarkus-artemis-bom</artifactId>
	        <version>3.2.1</version>
	        <type>pom</type>
	        <scope>import</scope>
	    </dependency>
```

Add the following dependencies to the dependencies section:
```
	<dependency>
	    <groupId>org.apache.camel.quarkus</groupId>
	    <artifactId>camel-quarkus-jms</artifactId>
	</dependency>
	<dependency>
        <groupId>io.quarkiverse.artemis</groupId>
        <artifactId>quarkus-artemis-jms</artifactId>
    </dependency>
```

Import `JmsArtemisIT.java` into the project.
Configure a `JmsComponent` such that the test is green

Hints
-----

```
quarkus.artemis.mybroker.url=tcp://localhost:61616
quarkus.artemis.mybroker.username=camel
quarkus.artemis.mybroker.password=camel
```

and
```
    @Named("jms")
    public JmsComponent jms(@Identifier("mybroker") ConnectionFactory connectionFactory) {
        return JmsComponent.jmsComponentClientAcknowledge(connectionFactory);
    }
```
