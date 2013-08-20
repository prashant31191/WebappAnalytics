package com.jsmix.labs.webappanalytics.ua;

import java.util.ArrayList;

import com.jsmix.labs.webappanalytics.cache.CacheMode;

public class UserAgentOptions extends ArrayList<UserAgent>{

	
	public static final ArrayList<UserAgent> LIST;
	
	static{
		LIST = new ArrayList<UserAgent>();
		LIST.add(new UserAgent("默认", null));
		LIST.add(new UserAgent("iPad", "Mozilla/5.0 (iPad; CPU OS 5_1 like Mac OS X; en-us) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9B176 Safari/7534.48.3"));
		LIST.add(new UserAgent("Android4", "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30"));
		LIST.add(new UserAgent("PC", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)"));		
	}
}
