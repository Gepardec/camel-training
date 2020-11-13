Creating Marshalling Route

Use a Json File as Input.

Marshal it to a xml.

Send this xml to a jms queue.

Test it with an IT: <br>
assertThat(exchange.getIn().getBody(String.class))
<br>.containsIgnoringCase("orderToProducer>")
<br>.containsIgnoringCase("amount>110</")....