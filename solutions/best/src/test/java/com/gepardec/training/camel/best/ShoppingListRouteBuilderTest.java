package com.gepardec.training.camel.best;

import com.gepardec.training.camel.best.domain.ShoppingList;
import com.gepardec.training.camel.best.domain.ShoppingListItem;
import com.gepardec.training.camel.commons.test.routetest.MockedEndpointId;
import com.gepardec.training.camel.commons.test.routetest.MockedRouteId;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.Uri;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.cdi.Beans;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(CamelCdiRunner.class)
@Beans(classes = ShoppingListRouteBuilder.class)
@MockedRouteId(ShoppingListRouteBuilder.ROUTE_ID)
public class ShoppingListRouteBuilderTest {

    @Inject
    private CamelContext camelContext;

    @Inject
    @Uri(ShoppingListRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
    private ProducerTemplate producerTemplate;

    @Inject
    @Uri("direct:triggerFileRoute")
    private ProducerTemplate producerTemplateTrigger;


    @Inject
    @Uri("mock:shopping_result")
    @MockedEndpointId(ShoppingListRouteBuilder.ENTRY_SEDA_ENDOINT_ID_FROM_JSON)
    private MockEndpoint shoppingResult;



    @Test
    public void writeIntoLogFileTest() throws IOException {
        List<ShoppingListItem> shoppingListItems = new ArrayList<>();
        shoppingListItems.add(new ShoppingListItem(2, 2));
        shoppingListItems.add(new ShoppingListItem(1, 10));
        shoppingListItems.add(new ShoppingListItem(3, 2));
        shoppingListItems.add(new ShoppingListItem(4, 0));
        shoppingListItems.add(new ShoppingListItem(2, 3));
        shoppingListItems.add(new ShoppingListItem(1, 0));
        ShoppingList shoppingList = new ShoppingList(shoppingListItems, "basics");

        producerTemplate.sendBody(shoppingList);
        String content = new String(Files.readAllBytes(Paths.get("src/test/resources/logs/logfile.txt")));
        assertThat(content).isNotEmpty();
        assertThat(content).contains("[{\"code\":2,\"amount\":2},{\"code\":1,\"amount\":10},{\"code\":3,\"amount\":2}");
    }

    @Ignore
    @Test
    public void readFromLogFileTest() throws Exception {

        producerTemplateTrigger.sendBody("test");
        shoppingResult.expectedMessageCount(1);
        shoppingResult.expects(() -> {
            final Exchange exchange = shoppingResult.getExchanges().stream().findFirst().orElse(null);

            Assert.assertNotNull(exchange);
        });

        // When
        producerTemplate.start();

        // Then
        shoppingResult.assertIsSatisfied();
    }
}

