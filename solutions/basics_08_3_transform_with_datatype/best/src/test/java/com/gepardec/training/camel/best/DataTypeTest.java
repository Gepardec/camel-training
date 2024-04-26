package com.gepardec.training.camel.best;

import org.apache.camel.EndpointInject;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.dataformat.JsonDataFormat;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.apache.camel.spi.DataType;
import org.apache.camel.spi.DataTypeAware;
import org.junit.jupiter.api.Test;

import com.gepardec.training.camel.commons.domain.ExampleOrders;
import com.gepardec.training.camel.commons.domain.OrderToProducer;
import com.gepardec.training.camel.commons.files.ExampleFiles;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class DataTypeTest extends CamelQuarkusTestSupport {

	private static final String TEST_NAME = "DataTypeTest";
	
    @Produce("direct:start" + TEST_NAME)
    private ProducerTemplate startProducer;

    @EndpointInject("mock:result" + TEST_NAME)
    private MockEndpoint resultEndpoint;

    
    @Override
    protected EndpointRouteBuilder createRouteBuilder() {
        return new EndpointRouteBuilder() {
            public void configure() throws Exception {
/*            	
            	JacksonDataFormat jsonOrderToProducer = new JacksonDataFormat(OrderToProducer.class);
            	jsonOrderToProducer.setPrettyPrint(true);
*/       
            	
				JsonDataFormat jsonOrderToProducer = 
									dataFormat()
									.json()
										.unmarshalType(OrderToProducer.class)
										.prettyPrint(true)
									.end();

				transformer()
					.fromType("json:OrderToProducer")
					.toType(OrderToProducer.class)
					.withDataFormat(jsonOrderToProducer);

				transformer()
					.fromType(OrderToProducer.class)
					.toType("json:OrderToProducer")
					.withDataFormat(jsonOrderToProducer);
/*				
				transformer().name("json:O2P")
					.withDataFormat(
							dataFormat()
							.json()
							.unmarshalType(OrderToProducer.class)
							.prettyPrint(true)
						.end());
				DataType jsonO2P = new DataType("json:O2P");
*/				
				
				
				
                from("direct:start" + TEST_NAME + "1") // In is "json:OrderToProducer"
                	.to(log("in direct:start").level("INFO"))
                	.process( printDataType("start"))
                	.validate(isJsonOrder())
                	.to("direct:transform2Java")
                	.validate(isJavaOrder())
                	.marshal().json(true)
                	.to(log("out direct:start").level("INFO"))
                	.to(resultEndpoint);
                
				
                from("direct:start" + TEST_NAME + "2") // In is java OrderToProducer
                	.to(log("in direct:start").level("INFO"))
                	.process( printDataType("start"))
                	.validate(isJavaOrder())
                	.to("direct:transform2Json")
                	.validate(isJsonOrder())
                	.to(log("out direct:start").level("INFO"))
                	.to(resultEndpoint);                
				
                from("direct:start" + TEST_NAME + "3") // In comes "json:OrderToProducer"
                	.to(log("in direct:start").level("INFO"))
                	.validate(isJsonOrder())
                	.to("direct:transform2JavaAndBack")
                	.validate(isJsonOrder())
                	.to(resultEndpoint);
                				
                from("direct:start" + TEST_NAME + "4") // In comes "json:OrderToProducer"
                	.to(log("in direct:start").level("INFO"))
                	.validate(isJsonOrder())
                	.transform(new DataType("json:OrderToProducer"), new DataType(OrderToProducer.class))
                	.validate(isJavaOrder())
                	.marshal().json(true)
                	.to(resultEndpoint);
                
                from("direct:transform2Java")
                	.inputType(OrderToProducer.class)
                	.process( printDataType("In transform2Java"))
                	.to(log("In direct transform").level("INFO"))
                	.validate(isJavaOrder());
                
                from("direct:transform2Json")
            		.inputType("json:OrderToProducer")
            		.process( printDataType("In transform2Json"))
            		.to(log("In direct transform2Json").level("INFO"))
            		.validate(isJsonOrder());
                
                from("direct:transform2JavaAndBack")
                	.inputType(OrderToProducer.class)
                	.outputType("json:OrderToProducer")
                	.to(log("In direct transform2JavaAndBack").level("INFO"))
                	.validate(isJavaOrder());
                                
            }

			private Processor printDataType(String msg) {
				return ex -> {
					String dataType = ((DataTypeAware) ex.getIn()).getDataType().toString();
					String bodyType = ex.getIn().getBody().getClass().getName();
					System.out.printf("!!!!!  %s  DataType: %s BodyType: %s \n", msg, dataType, bodyType);
					
				};
			}
			
			private Predicate isJsonOrder() {
				return ex -> {
					String dataType = ((DataTypeAware) ex.getIn()).getDataType().toString();
					String bodyType = ex.getIn().getBody().getClass().getName();
					return "json:OrderToProducer".equals(dataType) 
							&& ("java.lang.String".equals(bodyType)
									|| "org.apache.camel.converter.stream.InputStreamCache".equals(bodyType)
									);
				};
			}

			
			private Predicate isJavaOrder() {
				return ex -> {
					String dataType = ((DataTypeAware) ex.getIn()).getDataType().toString();
					String bodyType = ex.getIn().getBody().getClass().getName();
					return "java:com.gepardec.training.camel.commons.domain.OrderToProducer".equals(dataType) 
							&& "com.gepardec.training.camel.commons.domain.OrderToProducer".equals(bodyType);
				};
			}

        };
    }

    @Test
    public void test_transformer_toJava() throws InterruptedException {
        String msgIn = ExampleFiles.ORDER_TO_PRODUCER_EGGS_JSON;
        String msgExpected = msgIn;

        resultEndpoint.expectedBodiesReceived(msgExpected);

		sendTo("direct:start" + TEST_NAME + "1", msgIn, new DataType("json:OrderToProducer"));
    
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void test_transformer_toJson() throws InterruptedException {
        OrderToProducer msgIn = ExampleOrders.ORDER_TO_PRODUCER_EGGS;
        String msgExpected = ExampleFiles.ORDER_TO_PRODUCER_EGGS_JSON;

        resultEndpoint.expectedBodiesReceived(msgExpected);

		sendTo("direct:start" + TEST_NAME + "2", msgIn, new DataType(OrderToProducer.class));
    
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void test_java_in_json_out() throws InterruptedException {
        String msgIn = ExampleFiles.ORDER_TO_PRODUCER_EGGS_JSON;
        String msgExpected = msgIn;

        resultEndpoint.expectedBodiesReceived(msgExpected);

		sendTo("direct:start" + TEST_NAME + "3", msgIn, new DataType("json:OrderToProducer"));
    
        resultEndpoint.assertIsSatisfied();
    }
    
    @Test
    public void test_explicit_transformer() throws InterruptedException {
        String msgIn = ExampleFiles.ORDER_TO_PRODUCER_EGGS_JSON;
        String msgExpected = msgIn;

        resultEndpoint.expectedBodiesReceived(msgExpected);

		sendTo("direct:start" + TEST_NAME + "4", msgIn, new DataType("json:OrderToProducer"));
    
        resultEndpoint.assertIsSatisfied();
    }
    
    
	private void sendTo(String endpointUri, Object msgIn, DataType type) {
		startProducer.send(endpointUri, 
        		ex -> {
					((DataTypeAware)ex.getIn())
					.setBody(msgIn, type);
				}
        );
	}


}
