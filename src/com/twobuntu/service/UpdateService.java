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
import android.media.AudioManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.twobuntu.db.Article;
import com.twobuntu.db.ArticleHelper;
import com.twobuntu.db.ArticleProvider;
import com.twobuntu.twobuntu.ArticleListActivity;
import com.twobuntu.twobuntu.R;

// Periodically updates the internal database to reflect the current articles on the site.
public class UpdateService extends IntentService {
	
	// Extra data passed through the intent.
	public static String ARG_NOTIFICATIONS = "notifications";
	
	// Internal declarations.
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
	    // Begin building the notification.
		Notification notification = new Notification.Builder(this)
				.setAutoCancel(true)
		        .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, ArticleListActivity.class), 0))
		        .setContentTitle(getResources().getString(R.string.app_name))
		        .setContentText(article.getString("title"))
                .setSmallIcon(R.drawable.ic_stat_article).getNotification();
        // Play the sound if requested.
        String sound = mPreferences.getString("sound", "");
        if(sound.length() > 0) {
            notification.audioStreamType = AudioManager.STREAM_NOTIFICATION;
            notification.sound = Uri.parse(sound);
        }
        // Also vibrate if requested.
        if(mPreferences.getBoolean("vibrate", false))
            notification.defaults |= Notification.DEFAULT_VIBRATE;
        // Send the notification.
		((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).notify(article.getInt("id"),
				notification);
	}
	
	// TODO: there is a logic error with the code below. If more than twenty
	// articles are modified, some will miss getting updated because we set the
	// date of the last update to the current date and not the most recent date
	// obtained in one of the articles. This should eventually be fixed.
	
	// Processes the JSON received from the API.
	private void processArticles(boolean notifications, String json, long min) throws JSONException {
		JSONArray articles = new JSONArray(json);
		ArrayList<ContentValues> articlesForInsertion = new ArrayList<ContentValues>();
		// Used for storing the last modification time.
		long lastUpdate = 0;
		for(int i=0; i<articles.length(); ++i) {
			// Determine if the article exists in the database already.
			JSONObject article = articles.getJSONObject(i);
			Cursor cursor = mDatabase.getReadableDatabase().query(Article.TABLE_NAME, new String[]
					{ Article.COLUMN_ID }, Article.COLUMN_ID + " = " + article.getInt("id"),
					null, null, null, null);
			// If the article does NOT exist, add it and display a notification.
			if(cursor.getCount() == 0) {
				Log.i("UpdateService", "New article '" + article.getString("title") + "'.");
				articlesForInsertion.add(Article.convertToContentValues(article));
				if(notifications && article.getInt("date") >= min)
				    displayNotification(article);
			}
			// Otherwise, update the existing article in-place.
			else {
				Log.i("UpdateService", "Updating existing article " + article.getInt("id") + ".");
				Uri uri = Uri.withAppendedPath(ArticleProvider.CONTENT_LOOKUP_URI, String.valueOf(article.getInt("id")));
				getContentResolver().update(uri, Article.convertToContentValues(article), null, null);
			}
			// Store the most recent update.
			lastUpdate = Math.max(lastUpdate, article.getInt("date"));
		}
		// If any articles are queued for insertion, then insert them.
		if(articlesForInsertion.size() != 0)
			getContentResolver().bulkInsert(ArticleProvider.CONTENT_URI,
					articlesForInsertion.toArray(new ContentValues[articlesForInsertion.size()]));
		// Store the time of last update for the next poll.
		if(lastUpdate != 0)
		    mPreferences.edit().putLong(LAST_UPDATE, lastUpdate).commit();
	}
	
	// Process a single intent, which is simply a request to update the database.
	@Override
	protected void onHandleIntent(Intent intent) {
		// Determine if notifications should be shown.
		boolean notifications = intent.getBooleanExtra(ARG_NOTIFICATIONS, true);
		// Calculate the appropriate min / max parameters.
		long min = mPreferences.getLong(LAST_UPDATE, 0) + 1;
	    long max = System.currentTimeMillis() / 1000;
	    // Log the attempt.
		Log.i("UpdateService", "Performing scheduled update for range " + min + " - " + max + ".");
		try {
			// Read the raw JSON data from the server and parse it.
			String json = IOUtils.toString(new URL("https://" + DOMAIN_NAME + "/api/1.2/articles/?min=" + min +
					"&max=" + max).openStream(), "utf-8");
			processArticles(notifications, json, min);
		} catch (Exception e) {
			Log.e("UpdateService", "Error description: " + e.toString());
		} finally {
			// Schedule the next update.
			Log.i("UpdateService", "Scheduling next update.");
			Intent updateIntent = new Intent(this, UpdateService.class);
			long interval = Long.parseLong(mPreferences.getString("refresh_interval", "1800000"));
			((AlarmManager)getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.RTC,
					System.currentTimeMillis() + interval, PendingIntent.getService(this, 0, updateIntent, 0));
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
