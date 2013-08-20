package com.jsmix.labs.webappanalytics.ua;

public class UserAgent {
	private String text;
	private String value;
	
	public UserAgent(String text, String value) {
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

	public String getValue() {
		return value;
	}
}
