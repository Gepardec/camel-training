package com.gepardec.training.camel.best.domain;

public class ShoppingListItem {
    private int code;
    private int amount;

    public ShoppingListItem(int code, int amount) {
        this.code = code;
        this.amount = amount;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
