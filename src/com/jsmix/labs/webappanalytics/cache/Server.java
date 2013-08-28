package com.jsmix.labs.webappanalytics.cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import android.content.Context;
import android.util.Log;

public class Server {
	private final String TAG = Server.class.getName();
	private static final String REMOTE = "http://172.22.106.199:7777/remote/";
	private static final String MANIFEST = "manifest.json";
	private Context context;
	private String manifestString = "";

	public Server(Context context) {
		this.context = context;
	}

	private HttpEntity getManifestEntity() {
		Log.i(TAG, "开始连接 server...");
		HttpGet httpGet = new HttpGet(REMOTE + MANIFEST);
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);

		HttpResponse httpResponse = null;
		HttpEntity httpEntity = null;

		try {
			httpResponse = httpClient.execute(httpGet);
		} catch (ConnectTimeoutException e) {
			Log.e(TAG, "server 连接超时..." + e.getMessage());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (httpResponse == null) {
				Log.e(TAG, "server 连接出错...");
			} else if (httpResponse.getStatusLine().getStatusCode() == 200) {
				httpEntity = httpResponse.getEntity();
			} else {
				Log.e(TAG, "server 响应状态码异常 !");
			}
		}

		return httpEntity;
	}

	public Manifest getManifest() {
		HttpEntity httpEntity;
		Manifest manifest = null;
		InputStream manifestInputStream = null;

		httpEntity = getManifestEntity();

		if (httpEntity != null) {
			try {
				manifestInputStream = httpEntity.getContent();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (manifestInputStream != null) {
					Manifest manifestInst = new Manifest();
					manifest = manifestInst.getManifest(manifestInputStream);
					manifestString = manifestInst.getManifeString();
					try {
						manifestInputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return manifest;
	}

	private static void downloadFromUrl(URL url, File destFile)
			throws IOException {
		InputStream is = null;
		FileOutputStream fos = null;

		try {
			URLConnection urlConn = url.openConnection();

			is = urlConn.getInputStream();
			fos = new FileOutputStream(destFile);

			byte[] buffer = new byte[4096];
			int len;
			while ((len = is.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void downloadFile(String newFilePath, File destFile) {
		URL url;
		try {
			url = new URL(REMOTE + newFilePath);
			downloadFromUrl(url, destFile);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getManifestString() {
		return manifestString;
	}

}
