package com.gepardec.training.camel.best;

public class Hello {
	private String name;

	public Hello(String name) {
		this.name = name;
	}

	public String getName() {
		return "From bean: " + name;
	}
}
