package com.jsmix.labs.webappanalytics.webviewclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.R.bool;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jsmix.labs.webappanalytics.R;

public class LoadTestWebViewClient extends MainWebViewClient {

	private static final String TAG = "LoadTestWebViewClient";
	private Context context;
	private int resourcesNum = 0;
	private long timeStart;
	private long timeEnd;
	private boolean loaded = false;
	private boolean redirect = false;
	private int testTimesCounter = 0;
	private int testTimesCountLimit = 10;
	private TextView infoTextView;
	private boolean testCompleted = false;

	private List<Integer> loadTimeResult = new ArrayList<Integer>();
	private HashMap<Integer, Integer> firstLoadTimeResult = new HashMap<Integer, Integer>();
	private HashMap<Integer, String> firstLoadUrlResult = new HashMap<Integer, String>();
	
	public LoadTestWebViewClient(Context context) {
		super(context);
		this.context = context;

		Activity activity = (Activity) context;
		infoTextView = (TextView) activity.getLayoutInflater().inflate(
				R.layout.webview_global_tip, null);

		activity.addContentView(infoTextView, new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT));
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if (!loaded) {
			redirect = true;
		}

		loaded = false;
		view.loadUrl(url);
		return true;
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		loaded = false;
		resourcesNum = 0;
		Log.i(TAG, "onPageStarted.........");
		timeStart = System.currentTimeMillis();
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		if (!redirect) {
			loaded = true;
		}

		if (loaded && !redirect) {
			
			Log.i(TAG, "onPageFinished: " + url);
			
			timeEnd = System.currentTimeMillis();
			testTimesCounter++;

			int loadTime = (int) (timeEnd - timeStart);

			String resultText = "第"+testTimesCounter+"次测试，耗时: " + loadTime + ", Cache Mode: "
					+ view.getSettings().getCacheMode() + ", 首包: " + firstLoadTimeResult.get(testTimesCounter-1) + "ms";
			
			Log.i(TAG, resultText);
			
			infoTextView.setText(infoTextView.getText() + resultText + "\n");
			loadTimeResult.add(loadTime);

			if (testTimesCounter < testTimesCountLimit) {
				view.reload();
			} else {
				onTestComplete();
			}
		} else {
			redirect = false;
		}

	}

	@Override
	public void onLoadResource(WebView view, String url) {
		super.onLoadResource(view, url);
		
		if(!firstLoadTimeResult.containsKey(testTimesCounter)){
			long timeNow = System.currentTimeMillis();
			int timeCost = (int) (timeNow - timeStart);
			firstLoadTimeResult.put(testTimesCounter, timeCost);
			firstLoadUrlResult.put(testTimesCounter, url);
			Log.i(TAG, "first package: " + timeCost + "ms");
		}
		
		Log.i(TAG, "testTimesCounter: " + testTimesCounter);
		resourcesNum++;
	}

	public void onTestComplete() {
		testCompleted = true;
		
		int max = Collections.max(loadTimeResult);
		int min = Collections.min(loadTimeResult);
		int sum = 0;
		for (int loadTime : loadTimeResult) {
			sum += loadTime;
		}
		float avg = sum / testTimesCounter;

		String resultString = "测试完成：" + testTimesCounter + "次" + ", max: "
				+ max + ", min: " + min + ", avg: " + avg;
		Log.i(TAG, resultString);
		infoTextView.setText(infoTextView.getText() + "\n" +resultString + "\n");
		Toast.makeText(context, resultString, Toast.LENGTH_LONG).show();
	}

}
