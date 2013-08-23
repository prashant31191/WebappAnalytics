package com.jsmix.labs.webappanalytics.cloudpush;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.jsmix.labs.webappanalytics.ui.MainActivity;
import com.jsmix.labs.webappanalytics.ui.WebViewActivity;

/**
 * Push消息处理receiver
 */
public class PushMessageReceiver extends BroadcastReceiver {
	/** TAG to Log */
	public static final String TAG = PushMessageReceiver.class.getSimpleName();
	public static final String INTENT_TAG = "cloudpush";
	private static final String webReceiverURI = "file:///android_asset/web/buildin-1/cloudpushReceiver.html";
	AlertDialog.Builder builder;

	/**
	 * 
	 * 
	 * @param context
	 *            Context
	 * @param intent
	 *            接收的intent
	 */
	@Override
	public void onReceive(final Context context, Intent intent) {
		Log.d(TAG, ">>> Receive intent: \r\n" + intent.getAction());
		if (intent.getAction().equals(
				PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {
			Intent webviewIntent = new Intent();
			webviewIntent.setClass(context, WebViewActivity.class);
			webviewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			String title = intent
					.getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE);
			String content = intent
					.getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT);

			webviewIntent.putExtra(INTENT_TAG, true);
			webviewIntent.putExtra("buildinCase", webReceiverURI);
			webviewIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_TITLE,
					title);
			webviewIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT,
					content);
			Log.i(TAG, "Receive cloud push: " + title + " --- " + content);
			context.startActivity(webviewIntent);
		}
	}

}
