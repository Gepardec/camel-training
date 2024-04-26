package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.dto.OrderItemDto;

public class IncreaseAmountProcessor {
	
	OrderItemDto increase(OrderItemDto o) {
		o.setAmount(o.getAmount() +1);
		return o;
	}
}
