package com.jsmix.labs.webappanalytics.cache;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AssetsCacheAsyncTask extends AsyncTask<AssetsCacheAsyncTask, Void, AssetsCacheAsyncTask> {

	private final String TAG = AssetsCacheAsyncTask.class.getName();
	private Context context;
	
	public AssetsCacheAsyncTask(Context context) {
		this.context = context;
	}
	
	@Override
	protected AssetsCacheAsyncTask doInBackground(AssetsCacheAsyncTask... params) {
		End server = new Server(context);
		Manifest serverManifest = server.getManifest();
		Log.i(TAG, "获取远程 Manifest 成功: " + serverManifest.size());
		
		End client = new Client(context);
		Manifest clientManifest = client.getManifest();
		Log.i(TAG, "获取远程 Manifest 成功: " + clientManifest.size());
		
		Log.w(TAG, serverManifest.equals(clientManifest) ? "无更新" : "有更新");
		
		
		
		return null;
	}

}
