package com.gepardec.training.camel.best;

import com.gepardec.training.camel.best.domain.Order;
import com.gepardec.training.camel.commons.endpoint.CamelEndpoint;
import com.gepardec.training.camel.commons.test.routetest.CamelRouteTest;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EntryRouteBuilderTest extends CamelRouteTest {

    private static final String WRONG_JSON_FILE_PATH = "json/order_wrong_format.json";
    private static final String JSON_FILE_PATH = "json/order.json";

    @RouteUnderTest
    @InjectMocks
    private EntryRouteBuilder bestEntryRouteBuilder;

    @MockedEndpoint
    private CamelEndpoint mock = Endpoints.SPLITTER_ENTRY_SEDA_ENDPOINT;

    @Test
    public void wrongInputJson_ValidationFails() throws IOException {
        Assertions.assertThrows(CamelExecutionException.class, () -> {
            String wrongJson = getFileAsString(WRONG_JSON_FILE_PATH);
            sendToEndpoint(Endpoints.ENTRY_DIRECT_ENDPOINT, wrongJson);
        });
    }

    @Test
    public void correctInputJson_CorrectJavaObjectIsCreated() throws IOException {
        String json = getFileAsString(JSON_FILE_PATH);
        sendToEndpoint(Endpoints.ENTRY_DIRECT_ENDPOINT, json);
        Exchange exchange = pollFromEndpoint(mock);
        assertThat(exchange).isNotNull();
        Order order = exchange.getIn().getBody(Order.class);
        assertThat(order).isNotNull();
        assertThat(order.getPartnerId()).isEqualTo(1);
        assertThat(order.getItems()).hasSize(2);
    }

}