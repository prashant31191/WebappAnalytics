package com.jsmix.labs.webappanalytics.cache;

import java.util.ArrayList;

import android.webkit.WebSettings;


public class CacheModeOptions extends ArrayList<CacheMode>{

	private static final long serialVersionUID = 1L;
	
	public static final ArrayList<CacheMode> LIST;
	
	
	static{
		LIST = new ArrayList<CacheMode>();
		LIST.add(new CacheMode("默认", WebSettings.LOAD_DEFAULT));
		LIST.add(new CacheMode("仅使用缓存", WebSettings.LOAD_CACHE_ONLY));
		LIST.add(new CacheMode("不使用缓存", WebSettings.LOAD_NO_CACHE));		
		LIST.add(new CacheMode("使用缓存(忽略过期设置)", WebSettings.LOAD_CACHE_ELSE_NETWORK));
	}

}
