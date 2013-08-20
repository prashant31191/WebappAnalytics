package com.jsmix.labs.webappanalytics.webview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.webkit.WebView;

public class MainWebView extends WebView{

	private static final String TAG = "MainWebView";
	private Activity activity;
	
	public MainWebView(Activity activity) {
		super(activity);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
	}
}
