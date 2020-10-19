package com.gepardec.training.camel.best;

import com.gepardec.training.camel.best.domain.ShoppingList;
import com.gepardec.training.camel.best.domain.ShoppingListItem;
import com.gepardec.training.camel.commons.test.routetest.MockedRouteId;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.Uri;
import org.apache.camel.test.cdi.Beans;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(CamelCdiRunner.class)
@Beans(classes = ShoppingListRouteBuilder.class)
@MockedRouteId(ShoppingListRouteBuilder.ROUTE_ID)
public class ShoppingListRouteBuilderTest {

    @Inject
    @Uri(ShoppingListRouteBuilder.ENTRY_SEDA_ENDOINT_URI)
    private ProducerTemplate producerTemplate;

    @Test
    public void correctInput_messageInQueue() throws IOException {
        List<ShoppingListItem> shoppingListItems = new ArrayList<>();
        shoppingListItems.add(new ShoppingListItem(2, 2));
        shoppingListItems.add(new ShoppingListItem(1, 10));
        shoppingListItems.add(new ShoppingListItem(3, 2));
        shoppingListItems.add(new ShoppingListItem(4, 0));
        shoppingListItems.add(new ShoppingListItem(2, 3));
        shoppingListItems.add(new ShoppingListItem(1, 0));
        ShoppingList shoppingList = new ShoppingList(shoppingListItems, "basics");

        producerTemplate.sendBody(shoppingList);
        String content = Files.readString(Path.of("src/test/resources/logs/logfile.txt"), StandardCharsets.US_ASCII);
        assertThat(content).isNotEmpty();
        assertThat(content).contains("[{\"code\":2,\"amount\":2},{\"code\":1,\"amount\":10},{\"code\":3,\"amount\":2}");
    }

}
