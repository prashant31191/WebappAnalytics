package com.jsmix.labs.webappanalytics.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import android.R.integer;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class Client {

	private static final String TAG = Client.class.getName();
	private static final String ASSETS_PATH = "web/buildin-1/";
	private static final String ASSETS_CACHE_DIR = "assets";
	private static final String MANIFEST = "manifest.json";
	private Context context;
	private AssetManager assetManager;
	private String cacheRoot;
	private String cacheDirPath;
	private String manifestInAssetsPath;
	private String manifestInCachePath;
	private File cacheDir;

	public Client(Context context) {
		this.assetManager = context.getAssets();
		this.context = context;
		cacheRoot = context.getCacheDir().getPath();
		cacheDirPath = cacheRoot + File.separator
				+ ASSETS_CACHE_DIR;
		manifestInAssetsPath = ASSETS_PATH + MANIFEST;
		manifestInCachePath = cacheDirPath + File.separator + MANIFEST;
		cacheDir = new File(cacheDirPath);

		initCheck();
	}
	
	public String getAccessPath(){
		return cacheDirPath;
	}
	
	private void initCheck() {
		if (cacheDir.isDirectory()) {
			cacheDir.mkdir();
		}

		if (!hasCache()) {
			try {
				cpFileFromAssetsToCache(MANIFEST);
				Log.i(TAG, "初始化： 第一次写入 manifest");
				loadToCache(getManifest());
				Log.i(TAG, "初始化： 第一次写入 cache 文件");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void loadToCache(Manifest manifest) {
		Log.i(TAG, "获取本地 Manifest: " + manifest.size());
		try {
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
			inputStream = new FileInputStream(manifestInCachePath);
			manifest = new Manifest().getManifest(inputStream);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return manifest;
	}

	private void checkParentFolder(File file) {
		File parentFile = file.getParentFile();
		if (!parentFile.isDirectory()) {
			parentFile.mkdirs();
		}
	}

	private void cpFileFromAssetsToCache(String fileName) throws IOException {
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
		return new File(manifestInCachePath).isFile();
	}

	public void updateManifest(String manifestString) {
		FileWriter fw = null;
		try {
			Log.i(TAG, "manifestString" + manifestString);
			fw = new FileWriter(manifestInCachePath);
			fw.write(manifestString);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fw != null){
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void updateCache(Manifest oldManifest, Manifest newManifest) {
		try {
			for (int i = 0; i < oldManifest.size(); i++) {
				CacheFile cacheFileOld = oldManifest.get(i);
				CacheFile cacheFileNew = newManifest.get(i);

				if (cacheFileNew.getVersion() != cacheFileOld.getVersion()) {
					String newFilePath = cacheFileNew.getNewFilePath();
					File newFile = new File(cacheDirPath + File.separator
							+ cacheFileNew.getFileName());
					if (!newFile.exists()) {
						newFile.createNewFile();
					}

					Log.i(TAG, "开始更新文件: " + newFile.getPath());
					Server.downloadFile(newFilePath, newFile);
					Log.i(TAG, "更新文件完成: " + cacheFileNew.getFileName());

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
