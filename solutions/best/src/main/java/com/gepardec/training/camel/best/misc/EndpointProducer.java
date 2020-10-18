package com.gepardec.training.camel.best.misc;

import com.gepardec.training.camel.best.config.ConfigurationUtils;
import com.gepardec.training.camel.best.config.DbConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

@ApplicationScoped
public class EndpointProducer {

    @Produces
    @Named("JMSConnectionFactory")
    ActiveMQConnectionFactory createConnectionFactory() {
        return ConfigurationUtils.getJmsConnectionFactory("tcp://localhost:61616", "quarkus", "quarkus");
    }

    @Produces
    @Named("idGenerator")
    IdGenerator createIdGenerator() {
        return new IdGenerator();
    }

    @Produces
    @Named("postgres")
    DataSource createPostgresDatasource() throws IOException, SQLException {
        return DbConnection.getDatasource();
    }
}
