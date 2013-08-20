package com.jsmix.labs.webappanalytics.webviewclient;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainWebViewClient extends WebViewClient {

	private static final String TAG = "MainWebViewClient";
	
	public MainWebViewClient(Context context) {
	}
	
	@Override
	public void onLoadResource(WebView view, String url) {
		Log.i(TAG, "onLoadResrouce ------ " + url);
		super.onLoadResource(view, url);
	}

}
