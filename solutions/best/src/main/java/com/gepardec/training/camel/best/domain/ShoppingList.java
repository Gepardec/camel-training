package com.gepardec.training.camel.best.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ShoppingList {
    private List<ShoppingListItem>  shoppingListItems;
    @JsonProperty("name")
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
