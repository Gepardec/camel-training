package com.gepardec.training.camel.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.jms.ConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;

@ApplicationScoped
public class ConfigurationProducer {

    @Inject
    ConnectionFactory connectionFactory;

    @Named("jms")
    public JmsComponent jms() {
        return JmsComponent.jmsComponentClientAcknowledge(connectionFactory);
    }
}
