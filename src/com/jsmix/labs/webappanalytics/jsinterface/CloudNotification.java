package com.jsmix.labs.webappanalytics.jsinterface;

import android.R.string;

public class CloudNotification extends JSInterface {
	private String title;
	private String content;

	public CloudNotification(String title, String content) {
		super();
		this.title = title;
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
