package com.gepardec.training.camel.best.config;

import com.gepardec.training.camel.commons.endpoint.CamelEndpoint;

public final class Endpoints {
    public static final CamelEndpoint ENTRY_DIRECT_ENDPOINT = new CamelEndpoint("direct:best_entry", "best_entry");
    public static final CamelEndpoint SPLITTER_ENTRY_SEDA_ENDPOINT = new CamelEndpoint("seda:best_splitter_entry", "best_splitter_entry");
    public static final CamelEndpoint EGG_ORDER_ENTRY_SEDA_ENDPOINT = new CamelEndpoint("seda:egg_order_entry", "egg_order_entry");
    public static final CamelEndpoint PASTA_ORDER_ENTRY_SEDA_ENDPOINT = new CamelEndpoint("seda:pasta_order_entry", "pasta_order_entry");
    public static final CamelEndpoint MILK_ORDER_ENTRY_SEDA_ENDPOINT = new CamelEndpoint("seda:milk_order_entry", "milk_order_entry");
    public static final CamelEndpoint MEAT_ORDER_ENTRY_SEDA_ENDPOINT = new CamelEndpoint("seda:meat_order_entry", "meat_order_entry");

    public static final CamelEndpoint EGG_ORDER_JMS_ENDPOINT = new CamelEndpoint("jms:queue:eggs?disableReplyTo=true&username=quarkus&password=quarkus", "jms_queue_eggs");
    public static final CamelEndpoint PASTA_ORDER_SQL_ENDPOINT = new CamelEndpoint("sql:insert into order_to_producer (id, partner_id, item_code, item_count) values (:#${bean:idGenerator.nextId}, :#${body.partnerId}, :#${body.code}, :#${body.amount})?dataSource=#postgres", "sql_pasta");
}
