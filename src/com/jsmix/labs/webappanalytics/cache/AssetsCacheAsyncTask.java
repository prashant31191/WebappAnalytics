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

		Client client = new Client(context);
		Manifest clientManifest = client.getManifest();
		Log.i(TAG, "获取本地 Manifest 成功: " + clientManifest.size() +" 条缓存记录");
		
		Server server = new Server(context);
		Manifest serverManifest = server.getManifest();
		
		if(serverManifest != null){
			Log.i(TAG, "获取远程 Manifest 成功: " + serverManifest.size() +" 条缓存记录");
			
			Log.w(TAG, "远程与本地 Manifest 比对： " + (serverManifest.equals(clientManifest) ? "无更新" : "有更新"));
	
			if(!serverManifest.equals(clientManifest)){
				Log.i(TAG, "准备将服务器 Manifest 更新到客户端");
				client.updateCache(clientManifest, serverManifest);
				Log.i(TAG, "完成： 服务器 Manifest 更新到客户端");
				
				client.updateManifest(server.getManifestString());
			}
		}else{
			Log.w(TAG, "远程 Manifest 获取失败， 更新取消");
		}
		
		return null;
	}

}
