package com.jsmix.labs.webappanalytics.util.cookie;

public class Cookie {
	private String key;
	private String value;

	public Cookie(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
	
}
