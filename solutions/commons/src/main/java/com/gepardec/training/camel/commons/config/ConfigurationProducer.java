package com.gepardec.training.camel.commons.config;

import com.gepardec.training.camel.commons.config.ConfigurationUtils;
import com.gepardec.training.camel.commons.config.DbConnection;
import com.gepardec.training.camel.commons.misc.IdGenerator;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.context.annotation.Bean;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

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
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
        cf.setBrokerURL("tcp://localhost:61616");
        cf.setUserName("camel");
        cf.setPassword("camel");

        return JmsComponent.jmsComponentAutoAcknowledge(cf);
    }
}
