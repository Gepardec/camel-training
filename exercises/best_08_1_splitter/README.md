Creating Splitter Route
=======================

Prerequisites: Exercise 06.1

Split an order into a list of OrderToProducer items.

Steps
-----

Insert `SplitterTest.java` into the project.

Create a custom splitter:

```
public class OrderSplitter {

    public static List<OrderToProducer> splitOrder(Order order) {
        List<OrderToProducer> result = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
			result.add(new OrderToProducer(item, order.getPartnerId()));
		}
        return result;
    }
}
```

Integrate the splitter into the test route.

Make sure the tests are green.

Optional:

Log CamelSplitIndex and CamelSplitSize. What changes in the log when you use streaming?

