package com.twobuntu.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.twobuntu.db.Articles;
import com.twobuntu.db.ArticlesHelper;
import com.twobuntu.twobuntu.R;

// Periodically updates the internal database to reflect the current articles on the site.
public class UpdateService extends IntentService {
	
	private static final long UPDATE_INTERVAL = AlarmManager.INTERVAL_HALF_HOUR;
	private static final String LAST_UPDATE = "last_update";
	private static final String DOMAIN_NAME = "2buntu.com";

	// Async HTTP client used for all requests made.
	private AsyncHttpClient mClient;
	
	// The database we are updating.
	private ArticlesHelper mDatabase;
	
	// Access to shared preferences.
	private SharedPreferences mPreferences;
	
	// TODO: have the notification launch the appropriate intent
	
	// Adds a notification indicating that a new article was added.
	@SuppressWarnings("deprecation")
	private void displayNotification(JSONObject article) throws JSONException {
		Notification notification = new Notification.Builder(this)
		        .setContentTitle(getResources().getString(R.string.app_name))
		        .setContentText(article.getString("title"))
                .setSmallIcon(R.drawable.ic_stat_article).getNotification();
		((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).notify(0, notification);
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
				// If the article does NOT exist, add it and display a notification.
				if(cursor.getCount() == 0) {
					mDatabase.getWritableDatabase().insert(Articles.TABLE_NAME, null,
							Articles.convertToContentValues(article));
					displayNotification(article);
				}
				// Otherwise, update the existing article in-place.
				else
					mDatabase.getWritableDatabase().update(Articles.TABLE_NAME,
							Articles.convertToContentValues(article),
							Articles.COLUMN_ID + " = " + article.getInt("id"), null);
			}
			// Store the time of last update for the next poll.
			mPreferences.edit().putLong(LAST_UPDATE, max).commit();
		} catch (JSONException e) {
			Log.e("UpdateService", e.toString());
		}
	}
	
	public UpdateService() {
		super("UpdateService");
		mClient = new AsyncHttpClient();
		mDatabase = new ArticlesHelper(this);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	}
	
	// Process a single intent, which is simply a request to update the database.
	@Override
	protected void onHandleIntent(Intent intent) {
		final long min = mPreferences.getLong(LAST_UPDATE, 0) + 1;
		final long max = System.currentTimeMillis();
		mClient.get("http://" + DOMAIN_NAME + "/api/articles?min=" + min + "&max=" + max,
				new JsonHttpResponseHandler() {
			
			// Log the error and do nothing.
			@Override
			public void onFailure(Throwable e, String response) {
				Log.e("UpdateService", response);
			}
			
			@Override
			public void onSuccess(JSONObject response) {
				// First process all articles.
				processArticles(max, response);
				// Then schedule the next run.
				Intent intent = new Intent(UpdateService.this, UpdateService.class);
				((AlarmManager)getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.RTC,
						UPDATE_INTERVAL, PendingIntent.getBroadcast(UpdateService.this, 0, intent, 0));
			}
		});
	}
}
