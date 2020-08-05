My first Camel route
===================

Prereqitites: Maven, Java

Run
``` 
mvn archetype:generate \
  -DarchetypeGroupId=org.apache.camel.archetypes \
  -DarchetypeArtifactId=camel-archetype-spring \
  -DarchetypeVersion=3.4.2 \
  -DgroupId=at.gepardec.trainings.camel \
  -DartifactId=simple-routes
``` 
Then run

``` 
mvn camel:run
``` 

What happened?

