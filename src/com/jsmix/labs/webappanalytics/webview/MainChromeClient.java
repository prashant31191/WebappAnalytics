package com.jsmix.labs.webappanalytics.webview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.StaticLayout;
import android.util.Log;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;

public class MainChromeClient extends WebChromeClient {

	private Activity activity = null;
	private final static String TAG = "MainChromeClient";
	
	public MainChromeClient(Activity activity){
		this.activity = activity;
	}
	
	
	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		// TODO Auto-generated method stub
		super.onProgressChanged(view, newProgress);
		activity.setProgress(newProgress * 100);
	}


	@Override
	public void onReachedMaxAppCacheSize(long requiredStorage, long quota,
			QuotaUpdater quotaUpdater) {
		// TODO Auto-generated method stub
		super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
	}


	@Override
	public void onReceivedIcon(WebView view, Bitmap icon) {
		// TODO Auto-generated method stub
		super.onReceivedIcon(view, icon);
	}

	@Override
	public void onReceivedTitle(WebView view, String title) {
		super.onReceivedTitle(view, title);
		activity.setTitle(title);
	}
	
}
