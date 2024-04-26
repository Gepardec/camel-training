package com.gepardec.training.camel.best;

import org.apache.camel.EndpointInject;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Test;

import com.gepardec.training.camel.commons.domain.ExampleOrders;
import com.gepardec.training.camel.commons.domain.OrderToProducer;
import com.gepardec.training.camel.commons.files.ExampleFiles;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class SqlComponentiT extends CamelQuarkusTestSupport {
	
	private final String SQL_URI_INSERT="sql://insert into order_to_producer (id, partner_id, item_code, item_count)  values(CAST('238cc355-a89d-4428-b092-f0334aa1bb4f' as uuid), 1,3,123)";
	private final String SQL_URI_DELETE="sql://delete from order_to_producer";

	private final String MYPGSQL_URI_INSERT="mypgsql://insert into order_to_producer (id, partner_id, item_code, item_count)  values(CAST('238cc355-a89d-4428-b092-f0334aa1bb4f' as uuid) , 1,3,123)";
	private final String MYPGSQL_URI_DELETE="mypgsql://delete from order_to_producer";


	private static final String TEST_NAME = "SqlComponentiT";
	
    @Produce("direct:start" + TEST_NAME)
    private ProducerTemplate startProducer;

    @EndpointInject("mock:result" + TEST_NAME)
    private MockEndpoint resultEndpoint;

    
    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() throws Exception {

                from("direct:start" + TEST_NAME + "1")
                	.to(SQL_URI_DELETE)
                	.to(SQL_URI_INSERT)
                	.to("sql://select count(*) from order_to_producer;")
            		.to(resultEndpoint);               
                
                from("direct:start" + TEST_NAME + "2")
                	.to(MYPGSQL_URI_DELETE)
                	.to(MYPGSQL_URI_INSERT)
                	.to("sql://select count(*) from order_to_producer;")
                	.to(resultEndpoint);               

                from("direct:start" + TEST_NAME + "3")
            		.to(SQL_URI_DELETE)
            		.to("sql://insert into order_to_producer (id, partner_id, item_code, item_count)  " 
            				+ "values (CAST(:#${bean:uuidGen.nextId} AS uuid), :#${body.partnerId}, :#${body.code}, :#${body.amount})")
            		.to("sql://select count(*) from order_to_producer;")
            		.to(resultEndpoint);               

                from("direct:start" + TEST_NAME + "4")
            		.to(SQL_URI_DELETE)
            		.to(PastaOrderRouteBuilder.ENTRY_DIRECT_ENDOINT_URI)
            		.to("sql://select count(*) from order_to_producer;")
            		.to(resultEndpoint);               

            }
        };
    }

    @Test
    public void test_write_and_read_database() throws InterruptedException {
        OrderToProducer msgIn = ExampleOrders.ORDER_TO_PRODUCER_PASTA;
        String msgExpected = "[{count=1}]";

        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody("direct:start" + TEST_NAME + "1", msgIn);
        resultEndpoint.assertIsSatisfied();
    }
    
    @Test
    public void test_write_and_read_customised() throws InterruptedException {
        OrderToProducer msgIn = ExampleOrders.ORDER_TO_PRODUCER_PASTA;
        String msgExpected = "[{count=1}]";

        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody("direct:start" + TEST_NAME + "2", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void test_write_with_simple_language() throws InterruptedException {
        OrderToProducer msgIn = ExampleOrders.ORDER_TO_PRODUCER_PASTA;
        String msgExpected = "[{count=1}]";

        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody("direct:start" + TEST_NAME + "3", msgIn);
        resultEndpoint.assertIsSatisfied();
    }
    
    @Test
    public void test_write_from_body() throws InterruptedException {
        OrderToProducer msgIn = ExampleOrders.ORDER_TO_PRODUCER_PASTA;
        String msgExpected = "[{count=1}]";

        resultEndpoint.expectedBodiesReceived(msgExpected);

        startProducer.sendBody("direct:start" + TEST_NAME + "4", msgIn);
        resultEndpoint.assertIsSatisfied();
    }
}
