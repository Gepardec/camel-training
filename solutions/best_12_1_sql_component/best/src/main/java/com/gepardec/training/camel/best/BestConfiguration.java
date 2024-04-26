package com.gepardec.training.camel.best;

import javax.sql.DataSource;

import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.sql.SqlComponent;

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

    @Named("mypgsql")
    public SqlComponent mypgsql(@Named("my_db") DataSource dataSource) {
    	SqlComponent mydb = new SqlComponent();
    	mydb.setDataSource(dataSource);
		return mydb;
    }
}
