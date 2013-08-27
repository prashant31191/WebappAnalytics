package com.jsmix.labs.webappanalytics.cache;

import android.R.integer;

public class CacheFile {
	private String fileName;
	private int version;
	private String newFilePath;

	public String getNewFilePath() {
		return newFilePath;
	}

	public void setNewFilePath(String newFilePath) {
		this.newFilePath = newFilePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName.trim();
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	@Override
	public String toString() {
		return getFileName() +"."+ getVersion();
	}
	
}
