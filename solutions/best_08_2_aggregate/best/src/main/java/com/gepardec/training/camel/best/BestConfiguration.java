package com.gepardec.training.camel.best;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@ApplicationScoped
public class BestConfiguration {

	@Named
	OrderSplitter orderSplitter() {
		return new OrderSplitter();
	}
}
