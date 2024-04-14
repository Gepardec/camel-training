package com.gepardec.training.camel.best;

import org.apache.camel.component.jms.JmsComponent;

import io.smallrye.common.annotation.Identifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.jms.ConnectionFactory;

@ApplicationScoped
public class BestConfiguration {

	@Named
	OrderSplitter orderSplitter() {
		return new OrderSplitter();
	}

    @Named("jms")
    public JmsComponent jms(@Identifier("mybroker") ConnectionFactory connectionFactory) {
        return JmsComponent.jmsComponentClientAcknowledge(connectionFactory);
    }

}
