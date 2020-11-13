Creating Splitter Route
================================

Prerequisites: Exercise 07

Add a route which splits a List of OrderItems.

Make sure that the route splits only on demand and not upfront.

Send every split part to a seda endpoint, also log the CamelSplitIndex. (hint simple language)

Write a Test which checks: 
```
assertThat(result.getReceivedCounter()).hasSize(list.size);
```