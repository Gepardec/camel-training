Aggregate Strings
=================

Prerequisites: Exercise 06.1

Aggregate String-Messages with '+' as separator

Steps
-----

Write a StringAggregationStrategy that combines Strings with '+'

Write a new unit test with a RouteBuilder such that the following test succeeds:

```
    @Test
    public void test_aggregate_strings() throws InterruptedException {

        String msgExpected = "apple+cherry+orange+pear";

        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody("direct:start" + TEST_NAME + "1", "apple");
        startProducer.sendBody("direct:start" + TEST_NAME + "1", "cherry");
        startProducer.sendBody("direct:start" + TEST_NAME + "1", "orange");
        startProducer.sendBody("direct:start" + TEST_NAME + "1", "pear");

        resultEndpoint.assertIsSatisfied();
    }

```

