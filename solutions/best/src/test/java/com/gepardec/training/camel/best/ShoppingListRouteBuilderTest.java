package com.gepardec.training.camel.best;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.gepardec.training.camel.commons.domain.ShoppingList;
import com.gepardec.training.camel.commons.domain.ShoppingListItem;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class ShoppingListRouteBuilderTest extends CamelQuarkusTestSupport {

    @EndpointInject(ShoppingListRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
    private ProducerTemplate producerTemplate;

    @EndpointInject("direct:triggerFileRoute")
    private ProducerTemplate producerTemplateTrigger;

    @EndpointInject("mock:shopping_result")
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
        String content = new String(Files.readAllBytes(Paths.get("target/logs/logfile.txt")));
        assertNotNull(content);
        assertFalse(content.isEmpty());
        assertTrue(content.contains("[{\"code\":2,\"amount\":2},{\"code\":1,\"amount\":10},{\"code\":3,\"amount\":2}"));
    }
}
