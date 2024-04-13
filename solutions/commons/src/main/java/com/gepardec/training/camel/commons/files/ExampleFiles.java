package com.gepardec.training.camel.commons.files;

import java.io.IOException;

import org.apache.camel.RuntimeCamelException;

import com.gepardec.training.camel.commons.misc.FileUtils;

public class ExampleFiles {

	public static final String ORDER_EGGS_JSON = fileContentFrom("files/json/order_eggs.json");
    public static final String ORDER_TO_PRODUCER_EGGS_JSON = fileContentFrom("files/json/order_to_producer_eggs.json");
    public static final String ORDER_TO_PRODUCER_111_EGGS_JSON = fileContentFrom("files/json/order_to_producer_111_eggs.json");
    public static final String ORDER_TO_PRODUCER_EGGS_XML = fileContentFrom("files/xml/order_to_producer_eggs.xml");
    
    public static final String ORDER_JSON = fileContentFrom("files/json/order.json");
    public static final String ORDER_MEET_JSON = fileContentFrom("files/json/order_meat.json");
    public static final String ORDER_MILK_JSON = fileContentFrom("files/json/order_milk.json");
    public static final String ORDER_PASTA_JSON = fileContentFrom("files/json/order_pasta.json");
    public static final String ORDER_WRONG_FORMAT_JSON = fileContentFrom("files/json/order_wrong_format.json");
    public static final String ORDER_TO_PRODUCER_PASTA_JSON = fileContentFrom("files/json/order_to_producer_pasta.json");
    
	private static String fileContentFrom(String fileName) {
		try {
			String file = FileUtils.getFileAsString(fileName);
			if ( null == file ) {
				throw new RuntimeCamelException("Can't read file with name >" + fileName + "<");
			}
			return file;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
