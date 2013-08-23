package com.jsmix.labs.webappanalytics.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebView.FindListener;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.jsmix.labs.webappanalytics.R;
import com.jsmix.labs.webappanalytics.cloudpush.PushMessageReceiver;
import com.jsmix.labs.webappanalytics.jsinterface.JSInterface;
import com.jsmix.labs.webappanalytics.jsinterface.CloudNotification;
import com.jsmix.labs.webappanalytics.util.EasyToast;
import com.jsmix.labs.webappanalytics.webview.MainChromeClient;
import com.jsmix.labs.webappanalytics.webview.MainWebView;
import com.jsmix.labs.webappanalytics.webviewclient.LoadTestWebViewClient;
import com.jsmix.labs.webappanalytics.webviewclient.MainWebViewClient;

@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
public class WebViewActivity extends NormalActivity {

	private static final String TAG = "WebViewActivity";
	private WebView webView = null;
	private WebViewClient viewClient = null;
	private WebChromeClient chromeClient = null;
	private Toast toast = null;
	private final Activity activity = this;

	private double userPointX;
	private double userPointY;

	public double getUserPointX() {
		return userPointX;
	}

	public double getUserPointY() {
		return userPointY;
	}

	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Window window = getWindow();
		window.requestFeature(Window.FEATURE_PROGRESS);

		setContentView(R.layout.webview);

		Intent intent = getIntent();

		Bundle bundle = intent.getExtras();
		String url = bundle.getString("url");
		String userAgentString = bundle.getString("userAgentString");
		int cacheMode = bundle.getInt("cacheMode");
		boolean isLoadTest = bundle.getBoolean("loadTest");
		String buildinCase = bundle.getString("buildinCase");

		webView = new MainWebView(this);

		addContentView(webView, new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT));

		if (isLoadTest) {
			viewClient = new LoadTestWebViewClient(this);
		} else {
			viewClient = new MainWebViewClient(this);
		}

		chromeClient = new MainChromeClient(this);

		webView.setWebViewClient(viewClient);
		webView.setWebChromeClient(chromeClient);

		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		webSettings.setCacheMode(cacheMode);

		if (intent.getBooleanExtra(PushMessageReceiver.INTENT_TAG, false)) {
			JSInterface jsInterface = new CloudNotification(
					intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE),
					intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT));
			
			webView.addJavascriptInterface(jsInterface, "CloudNotification");
		}

		if (userAgentString != null) {
			webSettings.setUserAgentString(userAgentString);
		}

		if (buildinCase != null && !buildinCase.isEmpty()) {
			webView.loadUrl(buildinCase);
		} else {
			webView.loadUrl(url);
		}

		webView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				userPointX = event.getX();
				userPointY = event.getY();
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater menuInflater = new MenuInflater(activity);
		menuInflater.inflate(R.menu.webview_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		Log.i("Option", item.getItemId() + "");
		int itemId = item.getItemId();
		if (itemId == R.id.returnToHome) {
			Intent toHomeintent = new Intent();
			toHomeintent.setClass(activity, MainActivity.class);
			startActivity(toHomeintent);
			finish();
		} else if (itemId == R.id.option_cookie_mng) {
			Intent cookieMngIntent = new Intent();
			cookieMngIntent.setClass(activity, CookieMngActivity.class);
			cookieMngIntent.putExtra("url", webView.getUrl());
			startActivity(cookieMngIntent);
		} else if (itemId == R.id.option_reload_page) {
			webView.reload();
		}

		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (webView.canGoBack()) {
				webView.goBack();
			} else {
				if (toast != null) {
					toast.cancel();
				}
				toast = new EasyToast(activity, "不能再返回了");
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
