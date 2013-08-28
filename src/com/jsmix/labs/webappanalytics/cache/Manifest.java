package com.jsmix.labs.webappanalytics.cache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Manifest extends ArrayList<CacheFile> {

	private static final String TAG = Manifest.class.getName();
	private static final long serialVersionUID = -9220599891482584006L;
	private String manifeString = "";
	public Manifest() {
	
	}

	public Manifest getManifest(InputStream inputStream) {

		BufferedReader br = null;
		try {

			br = new BufferedReader(new InputStreamReader(inputStream));
			manifeString = "";
			String line = br.readLine();
			while (line != null) {
				manifeString += line;
				line = br.readLine();
			}
			
			Gson gson = new Gson();
			List<CacheFile> cacheFiles = gson.fromJson(manifeString,
					new TypeToken<List<CacheFile>>() {
					}.getType());
			
			this.addAll(cacheFiles);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
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

	public String getManifeString() {
		return manifeString;
	}

}
