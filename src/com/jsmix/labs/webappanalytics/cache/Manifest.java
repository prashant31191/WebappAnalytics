package com.jsmix.labs.webappanalytics.cache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.R.integer;
import android.R.string;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Manifest extends ArrayList<CacheFile> {

	private static final String TAG = Manifest.class.getName();
	private static final long serialVersionUID = -9220599891482584006L;
	private InputStream inputStream;

	public Manifest(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public Manifest getManifest() {

		BufferedReader br = null;
		try {

			br = new BufferedReader(new InputStreamReader(inputStream));
			String jsonString = "";
			String line = br.readLine();
			while (line != null) {
				jsonString += line;
				line = br.readLine();
			}
			Gson gson = new Gson();

			List<CacheFile> cacheFiles = gson.fromJson(jsonString,
					new TypeToken<List<CacheFile>>() {
					}.getType());

			for (CacheFile cacheFile : cacheFiles) {
				this.add(cacheFile);
			}

			Log.i(TAG, "Manifest 解析成功， 原始字符: " + jsonString);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return this;
	}

	public boolean equals(Manifest m) {
		String a = "";
		String b = "";
		if (this.size() != m.size()) {
			return false;
		}
		for (int i = 0; i < this.size(); i++) {
			a += this.get(i).toString();
			b += m.get(i).toString();
		}

		if (a.equals(b))
			return true;
		else
			return false;
	}

}
