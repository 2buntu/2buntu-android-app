package com.twobuntu.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.twobuntu.db.Articles;
import com.twobuntu.db.ArticlesHelper;

// Periodically updates the internal database to reflect the current articles on the site.
public class UpdateService extends IntentService {

	// A note about this class:
	// - failures are silently discarded
	
	private static final String LAST_UPDATE = "last_update";
	private static final String DOMAIN_NAME = "2buntu.com";

	// Async HTTP client used for all requests made.
	private AsyncHttpClient mClient;
	
	// The database we are updating.
	private ArticlesHelper mDatabase;
	
	// Access to shared preferences.
	private SharedPreferences mPreferences;
	
	// The latest update that we have against the API.
	private long mLastUpdate;
	
	// Adds an individual article to the database.
	private void addArticle(JSONObject article) {
		//...
	}
	
	// Updates an individual article in the database.
	private void updateArticle(JSONObject article) {
		//...
	}
	
	// Processes the JSON received from the API.
	private void processArticles(long max, JSONObject response) {
		try {
			JSONArray articles = response.getJSONArray("articles");
			for(int i=0; i<articles.length(); ++i) {
				// Determine if the article exists in the database already.
				JSONObject article = articles.getJSONObject(i);
				Cursor cursor = mDatabase.getReadableDatabase().query(Articles.TABLE_NAME, new String[]
						{ Articles.COLUMN_ID }, Articles.COLUMN_ID + " = " + article.getInt("id"),
						null, null, null, null);
				if(cursor.getCount() == 0)
					addArticle(article);
				else
					updateArticle(article);
			}
		} catch (JSONException e) {
			Log.e("UpdateService", e.toString());
		}
	}
	
	public UpdateService() {
		super("UpdateService");
		mClient = new AsyncHttpClient();
		mDatabase = new ArticlesHelper(getApplicationContext());
		mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		// Load the timestamp of the last update.
		mLastUpdate = mPreferences.getLong(LAST_UPDATE, 0);
	}
	
	// Process a single intent, which is simply a request to update the database.
	@Override
	protected void onHandleIntent(Intent intent) {
		final long min = mLastUpdate + 1;
		final long max = System.currentTimeMillis();
		mClient.get("http://" + DOMAIN_NAME + "/api/articles?min=" + min + "&max=" + max,
				new JsonHttpResponseHandler() {
			
			// Log the error and do nothing.
			@Override
			public void onFailure(Throwable e, String response) {
				Log.e("UpdateService", response);
			}
			
			// Begin processing the response.
			@Override
			public void onSuccess(JSONObject response) {
				processArticles(max, response);
			}
		});
	}
}
