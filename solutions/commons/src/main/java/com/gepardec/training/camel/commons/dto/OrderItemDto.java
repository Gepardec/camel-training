package com.gepardec.training.camel.commons.dto;

import java.util.Objects;

public class OrderItemDto {

    public static final int EGG = 1;
    public static final int PASTA = 2;
    public static final int MILK = 3;
    public static final int MEAT = 4;

    private long code;
    private int amount;

    public OrderItemDto() {
        code = 0L;
        amount = 0;
    }

    public OrderItemDto(long code, int amount) {
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

	@Override
	public int hashCode() {
		return Objects.hash(amount, code);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderItemDto other = (OrderItemDto) obj;
		return amount == other.amount && code == other.code;
	}

}
