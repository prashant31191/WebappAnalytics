package com.jsmix.labs.webappanalytics.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.StaticLayout;
import android.util.Log;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;

public class MainChromeClient extends WebChromeClient {

	private Context context;
	private final static String TAG = "MainChromeClient";
	
	public MainChromeClient(Context context){
		this.context = context;
	}
	
	
	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		// TODO Auto-generated method stub
		super.onProgressChanged(view, newProgress);
		((Activity) context).setProgress(newProgress * 100);
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
		((Activity) context).setTitle(title);
	}
	
	@Override
	public boolean onJsAlert(WebView view, String url, String message,
			JsResult result) {
				
		return super.onJsAlert(view, url, message, result);
	}
	
}
