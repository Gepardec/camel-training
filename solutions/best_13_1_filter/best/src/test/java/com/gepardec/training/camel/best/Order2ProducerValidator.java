package com.gepardec.training.camel.best;

import org.apache.camel.Body;
import org.apache.camel.Message;
import org.apache.camel.ValidationException;
import org.apache.camel.spi.DataType;

import com.gepardec.training.camel.commons.domain.OrderToProducer;

public class Order2ProducerValidator extends org.apache.camel.spi.Validator{

	@Override
	public void validate(Message message, DataType type) throws ValidationException {
		String typeName = type.getFullName();
	    if (message.getBody() instanceof OrderToProducer) {
			return;			
		}
		throw new ValidationException(message.getExchange(), "body ist not "  + OrderToProducer.class + " but " + message.getBody().getClass().getCanonicalName() );
	}

}
