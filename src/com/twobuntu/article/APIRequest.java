package com.twobuntu.article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

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
	
	// The HTTP client and associated cache used for all requests.
	private static AsyncHttpClient mClient = new AsyncHttpClient();
	
	// Attempts to invoke the specified callback.
	private static boolean invokeCallback(ResponseCallback callback, JSONObject response) {
		try {
			JSONArray items = response.getJSONArray("items");
			callback.onSuccess(items);
			return true;
		} catch (JSONException e) {
			callback.onFailure(e.toString());
			return false;
		}
	}
	
	// Attempts to load the specified page and invoke the specified callback when the request completes.
	public static void load(final String path, final ResponseCallback callback) {
		mClient.get("http://" + DOMAIN + "/api" + path, new JsonHttpResponseHandler() {
			
			// On failure, invoke the error callback.
			@Override
			public void onFailure(Throwable e, String response) {
				callback.onFailure(response);
			}
			
			// On success, invoke the success callback and store the data in the cache.
			@Override
			public void onSuccess(JSONObject response) {
				invokeCallback(callback, response);
			}
		});
	}
}
