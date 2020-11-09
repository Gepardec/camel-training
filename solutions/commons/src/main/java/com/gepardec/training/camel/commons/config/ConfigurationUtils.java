package com.gepardec.training.camel.commons.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;

public final class ConfigurationUtils {

    private ConfigurationUtils() {

    }

    public static void setupJmsConnectionFactory(CamelContext camelContext, String brokerUrl, String username, String password) {

        if (camelContext == null) {
            return;
        }

        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
        cf.setBrokerURL(brokerUrl);
        cf.setUserName(username);

        JmsComponent jmsComponent = JmsComponent.jmsComponentAutoAcknowledge(cf);
        if (camelContext.hasComponent("jms") == null) {
            camelContext.addComponent("jms", jmsComponent);
        }

    }

    public static ActiveMQConnectionFactory getJmsConnectionFactory(String brokerUrl, String username, String password) {

        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
        cf.setBrokerURL(brokerUrl);
        cf.setUserName(username);
        cf.setPassword(password);

        return cf;
    }
}
