package com.twobuntu.article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.twobuntu.twobuntu.R;

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
	
	// Attempts to load the specified page and invoke the specified callback when the request completes.
	public static void load(final Context context, String path, final ResponseCallback callback) {
		// TODO: maintain a cache!!!
		mClient.get("http://" + DOMAIN + "/api" + path, new JsonHttpResponseHandler() {
			
			// Display a progress dialog while the operation is underway.
			private ProgressDialog mProgressDialog = ProgressDialog.show(context,
					context.getResources().getString(R.string.dialog_title_please_wait),
					context.getResources().getString(R.string.dialog_loading_data));
			
			// On failure, pass the error message along to the calling code.
			@Override
			public void onFailure(Throwable e, String response) {
				mProgressDialog.dismiss();
				callback.onFailure(response);
			}
			
			// On success, pass the JSON array of items to the calling code.
			@Override
			public void onSuccess(JSONObject response) {
				mProgressDialog.dismiss();
				try {
					callback.onSuccess(response.getJSONArray("items"));
				} catch (JSONException e) {
					callback.onFailure(e.toString());
				}
			}
		});
	}
}
