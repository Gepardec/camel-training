package com.gepardec.training.camel.commons.config;

import java.io.IOException;
import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.sql.DataSource;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.context.annotation.Bean;

import com.gepardec.training.camel.commons.misc.IdGenerator;

@ApplicationScoped
public class ConfigurationProducer {

    @Produces
    @Named("JMSConnectionFactory")
    public ActiveMQConnectionFactory createConnectionFactory() {
        return ConfigurationUtils.getJmsConnectionFactory("tcp://localhost:61616", "camel", "camel");
    }

    @Produces
    @Named("idGenerator")
    public IdGenerator createIdGenerator() {
        return new IdGenerator();
    }

    @Produces
    @Named("postgres")
    public DataSource createPostgresDatasource() throws IOException, SQLException {
        return DbConnection.getDatasource();
    }

    @Bean
    public JmsComponent jms() {
        return JmsComponent.jmsComponentAutoAcknowledge(createConnectionFactory());
    }
}
