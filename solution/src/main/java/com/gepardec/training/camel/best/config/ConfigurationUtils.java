package com.gepardec.training.camel.best.config;

import io.quarkus.runtime.Startup;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

public final class ConfigurationUtils {

    private ConfigurationUtils(){

    }

    public static void setupJmsConnectionFactory(CamelContext camelContext, String brokerUrl, String username, String password) {

        if(camelContext == null){
            return;
        }

        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
        cf.setBrokerURL(brokerUrl);
        cf.setUserName(username);

        JmsComponent jmsComponent = JmsComponent.jmsComponentAutoAcknowledge(cf);
        camelContext.addComponent("jms", jmsComponent);
    }
}
