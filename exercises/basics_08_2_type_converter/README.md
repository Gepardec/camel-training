Type Converters
===============

Prerequisites: Exercise 08.1

Write a TypeConverter, that transforms `OrderItem` objects to `OrderItemDto` and back.

Steps
-----
Copy `TypeConverterTest.java` to your project.

```
public class OrderTypeConverters implements TypeConverters {

	@Converter
	public static OrderItemDto toOrderItemDto(OrderItem item) {
		// TODO: implement conversion
	}
	
	@Converter
	public static OrderItem toOrderItem(OrderItemDto dto) {
		// // TODO: implement conversion
	}
}

```

Register the converters in the Camel context of the test.

Verify that the all tests are green.
