package com.jsmix.labs.webappanalytics.cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

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

	private HttpEntity getManifestEntity() throws ClientProtocolException,
			IOException {
		HttpGet httpGet = new HttpGet(REMOTE + MANIFEST);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity httpEntity = null;
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			httpEntity = httpResponse.getEntity();
		} else {
			Log.e(TAG, "server connect fail !");
		}

		return httpEntity;
	}

	public Manifest getManifest() {
		HttpEntity httpEntity;
		Manifest manifest = null;
		InputStream manifestInputStream = null;
		try {
			httpEntity = getManifestEntity();
			manifestInputStream = httpEntity.getContent();
			Manifest manifestInst = new Manifest();
			manifest = manifestInst.getManifest(manifestInputStream);
			manifestString = manifestInst.getManifeString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				manifestInputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			} finally {
				if (fos != null) {
					fos.close();
				}
			}
		}
	}

	public static void downloadFile(String newFilePath, File destFile){
		URL url;
		try {
			url = new URL(REMOTE + newFilePath);
			downloadFromUrl(url, destFile);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getManifestString() {
		return manifestString;
	}
	
	
	
}
