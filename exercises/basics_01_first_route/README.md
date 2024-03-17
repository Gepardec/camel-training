My first Camel route
===================

Prerequisites: Maven, Java

Run
``` 
mvn archetype:generate \
  -DarchetypeGroupId=org.apache.camel.archetypes \
  -DarchetypeArtifactId=camel-archetype-spring \
  -DarchetypeVersion=4.4.0 \
  -DgroupId=at.gepardec.trainings.camel \
  -DartifactId=simple-routes
``` 
Then run

``` 
cd simple-routes/
mvn install
mvn camel:run
``` 

What happened?

Change the route such that messages with people from vienna are saved in a directory "at" and test it.

Create a Main-Class in order to start the routes from within the IDE

```
import org.apache.camel.spring.Main;

public class ApplicationSpringCamel {

     public static void main(String... args) throws Exception {
        Main.main(args);
    }
}
```
