package com.twobuntu.api;

import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;

import android.os.AsyncTask;

public class APIRequest extends AsyncTask<String, Void, JSONArray> {
    
	private static final String DOMAIN_NAME = "2buntu.com";
	
	@Override
	protected JSONArray doInBackground(String... endpoints) {
		try {
			InputStream inputStream = new URL("http://" + DOMAIN_NAME + "/api/1.1" + endpoints[0]).openStream();
			return new JSONArray(IOUtils.toString(inputStream, "utf-8"));
		} catch (Exception e) {
			return null;
		}
	}
}
