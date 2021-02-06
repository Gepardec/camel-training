package com.gepardec.trainings.camel.best;

import javax.enterprise.inject.Alternative;
import javax.inject.Named;
import javax.inject.Singleton;

import com.gepardec.training.camel.commons.domain.Order;

@Singleton
@Alternative
@Named("someBean")
public class OtherTestBean {

	int counter = 5;
	
    public Order someMethod(Order order) {
    	order.setPartnerId(order.getPartnerId() + counter);
        return order;
    }

}
