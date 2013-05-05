package com.twobuntu.twobuntu;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class APIRequest {
	
	// The domain name of the 2buntu website (putting it here allows
	// for local testing once rate limiting is implemented).
	private static String domain = "2buntu.com";
	
	// The HTTP client used for all requests.
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	// Attempts to load the specified page and invoke the specified callback
	// when the request completes.
	public static void load(String path, JsonHttpResponseHandler handler) {
		client.get("http://" + domain + "/api" + path, handler);
	}
}
