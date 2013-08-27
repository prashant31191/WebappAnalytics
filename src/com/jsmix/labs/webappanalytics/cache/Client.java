package com.jsmix.labs.webappanalytics.cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class Client extends End{

	private static final String TAG = Client.class.getName();
	
	private Context context;
	private AssetManager assetManager;
	private File cacheDir;

	public Client(Context context) {
		this.assetManager = context.getAssets();
		this.context = context;

		cacheDir = new File(context.getExternalCacheDir().getPath()
				+ File.separator + ASSETS_CACHE_DIR);

		if (cacheDir.isDirectory()) {
			cacheDir.mkdir();
		}

	}

	public void loadToCache(Manifest manifest) {
		try {
			Log.i(TAG, "获取本地 Manifest: " + manifest.size());
			for (CacheFile cacheFile : manifest) {
				cpFileFromAssetsToCache(cacheFile.getFileName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Manifest getManifest() {
		InputStream inputStream = null;
		Manifest manifest = null;
		try {
			inputStream = assetManager.open(ASSETS_PATH + "manifest.json");
			manifest = new Manifest(inputStream).getManifest();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return manifest;
	}

	public void checkParentFolder(File file) {
		File parentFile = file.getParentFile();
		if (!parentFile.isDirectory()) {
			parentFile.mkdirs();
		}
	}

	public void cpFileFromAssetsToCache(String fileName) throws IOException {
		String pathInCache = cacheDir + File.separator + fileName;
		File fileInCache = new File(pathInCache);
		checkParentFolder(fileInCache);
		if (!fileInCache.exists()) {
			fileInCache.createNewFile();
		}
		InputStream inputStream = null;
		FileOutputStream outputStream = null;

		inputStream = context.getAssets().open(ASSETS_PATH + fileName);
		outputStream = new FileOutputStream(pathInCache);

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}

		if (inputStream != null)
			inputStream.close();
		if (outputStream != null)
			outputStream.close();

		Log.i(TAG, "写入 assets 到 cache: " + fileName);
	}

	public boolean hasCache() {

		return true;
	}

	public void writeCache() {

	}

}
