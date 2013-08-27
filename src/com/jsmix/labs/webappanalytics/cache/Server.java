package com.jsmix.labs.webappanalytics.cache;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;

public class Server extends End{
	private final String TAG = Server.class.getName();
	private Context context;
	
	public Server(Context context) {
		this.context = context;
	}
	
	private HttpEntity getManifestEntity() throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet(REMOTE + MANIFEST);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity httpEntity = null;
		if(httpResponse.getStatusLine().getStatusCode() == 200){
			httpEntity = httpResponse.getEntity();
		}
		
		return httpEntity;
	}
	
	public Manifest getManifest() {
		HttpEntity httpEntity;
		InputStream inputStream = null;
		Manifest manifest = null;
		
		try {
			httpEntity = getManifestEntity();
			inputStream = httpEntity.getContent();
			manifest = new Manifest(inputStream).getManifest();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return manifest;
	}

}
