My first Camel route
===================

Prerequisites: Maven, Java

Run
``` 
mvn archetype:generate \
  -DarchetypeGroupId=org.apache.camel.archetypes \
  -DarchetypeArtifactId=camel-archetype-cdi \
  -DarchetypeVersion=3.7.1 \
  -DgroupId=com.gepardec.trainings.camel.best \
  -DartifactId=best
``` 
Then run

``` 
cd best
mvn install
mvn camel:run
``` 

Add Support for Main-Class:

```
    <weld3-version>3.0.5.Final</weld3-version>
...
    <!-- Start Camel with main -->
    <dependency>
        <groupId>org.apache.deltaspike.cdictrl</groupId>
        <artifactId>deltaspike-cdictrl-weld</artifactId>
        <version>${deltaspike-version}</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.jboss.weld.se</groupId>
        <artifactId>weld-se-shaded</artifactId>
        <version>${weld3-version}</version>
        <scope>runtime</scope>
    </dependency>
```

```
package com.gepardec.trainings.camel.best;

import org.apache.camel.cdi.Main;

public class ApplicationCdiCamel {

	    public static void main(String... args) throws Exception {
	    	new Main().run(args);;
	    }
	}
```
