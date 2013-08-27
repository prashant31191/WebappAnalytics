package com.jsmix.labs.webappanalytics.cache;

import android.content.Context;

public abstract class End {
	
	protected static final String ASSETS_PATH = "web/buildin-1/";
	protected static final String ASSETS_CACHE_DIR = "assets";
	protected static final String REMOTE = "http://192.168.1.115:7777/remote/";
	protected static final String MANIFEST = "manifest.json";
	
	
	abstract public Manifest getManifest();
	
}
