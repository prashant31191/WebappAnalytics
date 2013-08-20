package com.jsmix.labs.webappanalytics.util.cookie;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

public class CookieMng extends HashMap<String, String>{
	
	private static final long serialVersionUID = 1L;
	private String cookiesString;
	private Set<String> cookieKeySet;
	private Iterator<String> iterator;
	
	public CookieMng(String cookiesString) {
		this.cookiesString = cookiesString;
		parse();
	}
	
	public CookieMng parse() {
		String[] cookiesStringArray = Pattern.compile(";").split(cookiesString);
		for(String cookieString : cookiesStringArray){
//			Log.i("cookie", cookie.trim());
			cookieString = cookieString.trim();
			if(cookieString.contains("=")){
				String[] cookieStringArray = cookieString.split("=");
				String name = cookieStringArray[0];
				String value = cookieStringArray[1];
				put(name, value);
			}
		}
		
		cookieKeySet = keySet();
		iterator = cookieKeySet.iterator();
		return this;
	}

	public boolean hasNextKey() {
		return iterator.hasNext();
	}

	public String nextKey() {
		return iterator.next();
	}

	public void remove() {
		
	}
	
	public void add(String key, String value) {
		
	}
	
}
