package com.gepardec.training.camel.commons.domain;

public class OrderItem {

    public static final int EGG = 1;
    public static final int PASTA = 2;
    public static final int MILK = 3;
    public static final int MEAT = 4;

    private long code;
    private int amount;

    public OrderItem() {
        code = 0L;
        amount = 0;
    }

    public OrderItem(long code, int amount) {
        this.code = code;
        this.amount = amount;
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
