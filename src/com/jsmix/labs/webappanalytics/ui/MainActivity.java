package com.jsmix.labs.webappanalytics.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.jsmix.labs.webappanalytics.R;
import com.jsmix.labs.webappanalytics.cache.CacheMode;
import com.jsmix.labs.webappanalytics.cache.CacheModeOptions;
import com.jsmix.labs.webappanalytics.ua.UserAgent;
import com.jsmix.labs.webappanalytics.ua.UserAgentOptions;

public class MainActivity extends NormalActivity {

	private static final String TAG = "MainActivity";
	private EditText urlInput;
	private Button confirmButotn;
	private Spinner userAgentSpinner;
	private Spinner cacheModeSpinner;
	private Button qrButton;
	private Intent loadWebViewIntent = new Intent();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		urlInput = (EditText) findViewById(R.id.url_edit_text);
		confirmButotn = (Button) findViewById(R.id.url_confirm_button);

		userAgentSpinner = (Spinner) findViewById(R.id.user_agent_spinner);
		userAgentSpinner.setAdapter(new ArrayAdapter<UserAgent>(this,
				android.R.layout.simple_spinner_dropdown_item,
				UserAgentOptions.LIST));

		cacheModeSpinner = (Spinner) findViewById(R.id.cache_mode_spinner);
		cacheModeSpinner.setAdapter(new ArrayAdapter<CacheMode>(this,
				android.R.layout.simple_spinner_dropdown_item,
				CacheModeOptions.LIST));

		qrButton = (Button) findViewById(R.id.get_qr_url_btn);

		setEvent();

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void onConfirm() {
		String url = urlInput.getText().toString();

		UserAgent userAgent = (UserAgent) userAgentSpinner.getSelectedItem();
		CacheMode cacheMode = (CacheMode) cacheModeSpinner.getSelectedItem();

		loadWebViewIntent.setClass(MainActivity.this, WebViewActivity.class);
		loadWebViewIntent.putExtra("url", url);
		loadWebViewIntent.putExtra("userAgentString", userAgent.getValue());
		loadWebViewIntent.putExtra("cacheMode", cacheMode.getValue());
		finish();
		startActivity(loadWebViewIntent);
	}

	private void setEvent() {
		confirmButotn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onConfirm();
			}
		});

		urlInput.setOnEditorActionListener(new OnEditorActionListener() {

			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				onConfirm();
				return false;
			}
		});

		findViewById(R.id.load_test_confirm).setOnClickListener(
				new OnClickListener() {

					public void onClick(View v) {
						loadWebViewIntent.putExtra("loadTest", true);
						onConfirm();
					}
				});

		findViewById(R.id.load_test_confirm).setOnLongClickListener(
				new OnLongClickListener() {

					public boolean onLongClick(View v) {
						loadWebViewIntent.putExtra("loadTest", true);
						loadWebViewIntent
								.putExtra("buildinCase",
										"file:///android_asset/web/buildin-1/index.html");
						onConfirm();
						return false;
					}
				});

		qrButton.setOnClickListener(new OnClickListener() {
			// TODO
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction("com.google.zxing.client.android.SCAN");
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
				intent.setClass(MainActivity.this, CaptureActivity.class);
				
				startActivityForResult(intent, 0);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater menuInflater = new MenuInflater(this);
		menuInflater.inflate(R.menu.startview_menu, menu);
		return true;
	}

	public void clearUrlText(View view) {
		urlInput.setText("http://");
		urlInput.setSelection(urlInput.getText().length());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				// Handle successful scan
				String capturedResult = intent.getStringExtra("SCAN_RESULT");
				urlInput.setText(capturedResult);

			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
			}
		} else {

		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

}
