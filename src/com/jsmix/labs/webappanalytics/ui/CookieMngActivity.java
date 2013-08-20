package com.jsmix.labs.webappanalytics.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jsmix.labs.webappanalytics.util.cookie.CookieMng;
import com.jsmix.labs.webappanalytics.R;

public class CookieMngActivity extends NormalActivity {
	
	private CookieManager cookieManager;
	private ListView listView;
	private CookieMng cookieMng;
	private List<HashMap<String, String>> cookieList = new ArrayList<HashMap<String,String>>();
	private ArrayList<String> cookieKeyValueList = new ArrayList<String>();
	private LayoutInflater layoutInflater;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cookie_mng);
		Intent intent = getIntent();
		String url = (String) intent.getExtras().get("url");
		cookieManager = CookieManager.getInstance();
		listView = (ListView)findViewById(R.id.cookie_mng_listview);
		layoutInflater = getLayoutInflater();
		
		String cookiesString = cookieManager.getCookie(url);
		cookieMng = new CookieMng(cookiesString);
		
		
		if(!cookieMng.isEmpty()){
			int index = 0;
			while (cookieMng.hasNextKey()) {
				String key = cookieMng.nextKey();
				String value = cookieMng.get(key);
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("key", key);
				hashMap.put("value", value);
				cookieList.add(hashMap);
				
				cookieKeyValueList.add(key +" : "+ value);
			}
			
//			ArrayAdapter<String> cookieAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cookieKeyValueList);
//			listView.setAdapter(cookieAdapter);
			
			cookieMngListViewAdapter adapter = new cookieMngListViewAdapter();
			listView.setAdapter(adapter);
			
		}
		
	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
	
	
	
	private class cookieMngListViewAdapter extends BaseAdapter {

		public int getCount() {
			// TODO Auto-generated method stub
			return cookieList.size();
		}

		public Object getItem(int position) {
			return cookieList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View cookieListView = layoutInflater.inflate(R.layout.cookie_mng_list, null);
			TextView titleTextView = (TextView) cookieListView.findViewById(R.id.cookie_mng_listview_title);
			TextView contentTextView = (TextView) cookieListView.findViewById(R.id.cookie_mng_listview_content);
			titleTextView.setText(cookieList.get(position).get("key"));
			contentTextView.setText(cookieList.get(position).get("value"));
			
			return cookieListView;
		}
		
	}
	
	
}
