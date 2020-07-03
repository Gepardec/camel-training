package com.gepardec.training.camel.best.domain;

public class OrderItem {
    long code;
    int amount;

    public OrderItem(){
        code = 0;
        amount = 0;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
