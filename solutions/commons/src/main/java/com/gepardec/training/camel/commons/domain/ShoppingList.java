package com.gepardec.training.camel.commons.domain;

import java.util.List;

public class ShoppingList {
    private List<ShoppingListItem> shoppingListItems;
    private String listName;

    public ShoppingList(List<ShoppingListItem> shoppingListItems, String listName) {
        this.shoppingListItems = shoppingListItems;
        this.listName = listName;
    }

    public List<ShoppingListItem> getShoppingListItems() {
        return shoppingListItems;
    }

    public void setShoppingListItems(List<ShoppingListItem> shoppingListItems) {
        this.shoppingListItems = shoppingListItems;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }
}
