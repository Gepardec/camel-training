package com.gepardec.training.camel.commons.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionLoggingProcessor implements Processor {
  private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionLoggingProcessor.class);

  public static ExceptionLoggingProcessor create() {
	return new ExceptionLoggingProcessor();
  }

  @Override
  public void process(Exchange exchange) throws Exception {
	if (exchange.getException() != null) {
	  setBody(exchange, exchange.getException());
	} else if (exchange.getProperty(Exchange.EXCEPTION_CAUGHT) != null) {
	  setBody(exchange, exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class));
	}

  }

  private void setBody(Exchange exchange, Exception exception) {
	String routeId = exchange.getFromRouteId();
	LOGGER.error(String.format("Exception occured in %s: %s", routeId, exception.getMessage()), exception);
	exchange.getIn().setBody(getExceptionStackTrace(exception));
  }

  private String getExceptionStackTrace(Exception exception) {
	StringWriter stringWriter = new StringWriter();
	exception.printStackTrace(new PrintWriter(stringWriter));
	return stringWriter.toString();
  }

}
