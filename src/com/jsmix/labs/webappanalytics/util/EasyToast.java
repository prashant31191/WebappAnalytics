package com.jsmix.labs.webappanalytics.util;

import android.content.Context;
import android.widget.Toast;

public class EasyToast extends Toast{

	public EasyToast(Context context, String text) {
		super(context);
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public EasyToast(Context context, String text, int time) {
		super(context);
		Toast.makeText(context, text, time).show();
		
	}
	
}
