package com.twobuntu.article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.twobuntu.cache.Cache;

// Wraps calls to the 2buntu API.
public class APIRequest {
	
	// Provides notification when an array of items is returned by the API.
	public interface ResponseCallback {
		public void onFailure(String error);
		public void onSuccess(JSONArray items) throws JSONException;
	}
	
	// The domain name of the 2buntu website (putting it here allows
	// for local testing once rate limiting is implemented, etc.).
	private static final String DOMAIN = "2buntu.com";
	
	// The HTTP client used for all requests.
	private static AsyncHttpClient mClient = new AsyncHttpClient();
	
	// Cache used for storing data.
	private static Cache<JSONArray> mCache = new Cache<JSONArray>();
	
	// Attempts to load the specified page and invoke the specified callback when the request completes.
	public static void load(final Context context, final String path, final ResponseCallback callback) {
		// First check to see if the data was cached.
		JSONArray value = mCache.get(path);
		if(value != null) {
			try {
				callback.onSuccess(value);
			} catch (JSONException e) {
				callback.onFailure(e.toString());
			}
			return;
		}
		// It's not in the cache, so fetch the data.
		mClient.get("http://" + DOMAIN + "/api" + path, new JsonHttpResponseHandler() {
			
			// On failure, pass the error message along to the calling code.
			@Override
			public void onFailure(Throwable e, String response) {
				callback.onFailure(response);
			}
			
			// On success, pass the JSON array of items to the calling code.
			@Override
			public void onSuccess(JSONObject response) {
				try {
					JSONArray items = response.getJSONArray("items");
					callback.onSuccess(items);
					mCache.set(path, items);
				} catch (JSONException e) {
					callback.onFailure(e.toString());
				}
			}
		});
	}
}
