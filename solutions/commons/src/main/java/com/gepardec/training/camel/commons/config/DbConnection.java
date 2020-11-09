package com.gepardec.training.camel.commons.config;

import io.agroal.api.AgroalDataSource;
import io.agroal.api.configuration.supplier.AgroalPropertiesReader;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DbConnection {

    public static DataSource getDatasource() throws IOException, SQLException {
        Map<String,String> props=new HashMap<>();
        Properties properties = new Properties();
        properties.load(DbConnection.class.getResourceAsStream("/application.properties"));

        props.put(AgroalPropertiesReader.MAX_SIZE, properties.getProperty("datasource.postgres.jdbc.max-size", "10"));
        props.put(AgroalPropertiesReader.MIN_SIZE,  properties.getProperty("datasource.postgres.jdbc.min-size", "10"));
        props.put(AgroalPropertiesReader.JDBC_URL, properties.getProperty("datasource.postgres.jdbc.url"));
        props.put(AgroalPropertiesReader.PRINCIPAL, properties.getProperty("datasource.postgres.username"));
        props.put(AgroalPropertiesReader.CREDENTIAL, properties.getProperty("datasource.postgres.password"));

        return AgroalDataSource.from(new AgroalPropertiesReader()
                .readProperties(props).get());

    }
}