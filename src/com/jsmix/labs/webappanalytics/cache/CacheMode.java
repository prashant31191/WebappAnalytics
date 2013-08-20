package com.jsmix.labs.webappanalytics.cache;

public class CacheMode {
	private String text;
	private int value;
	
	public CacheMode(String text, int value) {
		this.text = text;
		this.value = value;
	}

	@Override
	public String toString() {
		return text;
	}

	public String getText() {
		return text;
	}

	public int getValue() {
		return value;
	}
	
	
}
