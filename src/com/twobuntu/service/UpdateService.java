package com.twobuntu.service;

import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.twobuntu.db.ArticleHelper;
import com.twobuntu.db.ArticleProvider;
import com.twobuntu.db.Articles;
import com.twobuntu.twobuntu.R;

// Periodically updates the internal database to reflect the current articles on the site.
public class UpdateService extends IntentService {
	
	private static final long UPDATE_INTERVAL = AlarmManager.INTERVAL_HALF_HOUR;
	private static final String LAST_UPDATE = "last_update";
	private static final String DOMAIN_NAME = "2buntu.com";
	
	// The database we are updating.
	private ArticleHelper mDatabase;
	
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
		((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).notify(article.getInt("id"),
				notification);
	}
	
	// TODO: there is a logic error with the code below. If more than twenty
	// articles are modified, some will miss getting updated because we set the
	// date of the last update to the current date and not the most recent date
	// obtained in one of the articles. This should eventually be fixed.
	
	// Processes the JSON received from the API.
	private void processArticles(long max, JSONObject response) throws JSONException {
		JSONArray articles = response.getJSONArray("articles");
		ArrayList<ContentValues> articlesForInsertion = new ArrayList<ContentValues>();
		for(int i=0; i<articles.length(); ++i) {
			// Determine if the article exists in the database already.
			JSONObject article = articles.getJSONObject(i);
			Cursor cursor = mDatabase.getReadableDatabase().query(Articles.TABLE_NAME, new String[]
					{ Articles.COLUMN_ID }, Articles.COLUMN_ID + " = " + article.getInt("id"),
					null, null, null, null);
			// If the article does NOT exist, add it and display a notification.
			if(cursor.getCount() == 0) {
				articlesForInsertion.add(Articles.convertToContentValues(article));
				displayNotification(article);
			}
			// Otherwise, update the existing article in-place.
			else {
				Uri uri = Uri.withAppendedPath(ArticleProvider.CONTENT_LOOKUP_URI, String.valueOf(article.getInt("id")));
				getContentResolver().update(uri, Articles.convertToContentValues(article), null, null);
			}
		}
		// If any articles are queued for insertion, then insert them.
		if(articlesForInsertion.size() != 0)
			getContentResolver().bulkInsert(ArticleProvider.CONTENT_URI,
					articlesForInsertion.toArray(new ContentValues[articlesForInsertion.size()]));
		// Store the time of last update for the next poll.
		mPreferences.edit().putLong(LAST_UPDATE, max).commit();
	}
	
	// Process a single intent, which is simply a request to update the database.
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i("UpdateService", "Performing scheduled update.");
		// Calculate the appropriate min / max parameters.
		long min = mPreferences.getLong(LAST_UPDATE, 0) + 1;
	    long max = System.currentTimeMillis() / 1000;
		try {
			// Read the raw JSON data from the server and parse it.
			String json = IOUtils.toString(new URL("http://" + DOMAIN_NAME + "/api/articles?min=" + min +
					"&max=" + max + "&sort=newest").openStream(), "utf-8");
			processArticles(max, new JSONObject(json));
		} catch (Exception e) {
			Log.e("UpdateService", "Error description: " + e.toString());
		} finally {
			// Schedule the next update.
			Log.i("UpdateService", "Scheduling next update.");
			Intent updateIntent = new Intent(this, UpdateService.class);
			((AlarmManager)getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.RTC,
					UPDATE_INTERVAL, PendingIntent.getBroadcast(this, 0, updateIntent, 0));
		}
	}
	
	public UpdateService() {
		super("UpdateService");
	}
	
	// Initialize the article database and shared preferences.
	@Override
	public void onCreate() {
		super.onCreate();
		mDatabase = new ArticleHelper(this);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	}
}
