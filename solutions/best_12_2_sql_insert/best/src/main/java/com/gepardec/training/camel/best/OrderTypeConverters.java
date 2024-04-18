package com.gepardec.training.camel.best;

import org.apache.camel.Converter;
import org.apache.camel.TypeConverters;

import com.gepardec.training.camel.commons.domain.OrderItem;
import com.gepardec.training.camel.commons.dto.OrderItemDto;

@Converter
public class OrderTypeConverters implements TypeConverters {

	@Converter
	public static OrderItemDto toOrderItemDto(OrderItem item) {
		return new OrderItemDto(item.getCode(), item.getAmount());		
	}
	
	@Converter
	public static OrderItem toOrderItem(OrderItemDto dto) {
		return new OrderItem(dto.getCode(), dto.getAmount());		
	}
}
