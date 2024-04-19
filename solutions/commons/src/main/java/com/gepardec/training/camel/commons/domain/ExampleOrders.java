package com.gepardec.training.camel.commons.domain;

import java.util.List;

public class ExampleOrders {

	private static final int PARTNER_ID = 1;

	public static final OrderItem ORDER_ITEM_EGGS = new OrderItem(OrderItem.EGG, 110);
	public static final OrderItem ORDER_ITEM_PASTA = new OrderItem(OrderItem.PASTA, 120);
	public static final OrderItem ORDER_ITEM_MILK = new OrderItem(OrderItem.MILK, 130);
	public static final OrderItem ORDER_ITEM_MEAT = new OrderItem(OrderItem.MEAT, 140);
	public static final OrderItem ORDER_ITEM_MEAT_90 = new OrderItem(OrderItem.MEAT, 90);

	public static final Order ORDER_MIXED = new Order(PARTNER_ID, List.of(ORDER_ITEM_EGGS, ORDER_ITEM_PASTA, ORDER_ITEM_MILK, ORDER_ITEM_MEAT));

	public static final OrderToProducer ORDER_TO_PRODUCER_EGGS = new OrderToProducer(ORDER_ITEM_EGGS, PARTNER_ID);
	public static final OrderToProducer ORDER_TO_PRODUCER_PASTA = new OrderToProducer(ORDER_ITEM_PASTA, PARTNER_ID);
	public static final OrderToProducer ORDER_TO_PRODUCER_MILK = new OrderToProducer(ORDER_ITEM_MILK, PARTNER_ID);
	public static final OrderToProducer ORDER_TO_PRODUCER_MEAT = new OrderToProducer(ORDER_ITEM_MEAT, PARTNER_ID);
	public static final OrderToProducer ORDER_TO_PRODUCER_MEAT_90 = new OrderToProducer(ORDER_ITEM_MEAT_90, PARTNER_ID);


}
