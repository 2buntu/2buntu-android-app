package com.twobuntu.db;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;

// Represents a single article for display within the application.
public class Article {
	
	// The name of the articles table.
	public static final String TABLE_NAME = "article";
	
	// The columns in the article table.
	public static final String COLUMN_ID                     = "_id";
	public static final String COLUMN_AUTHOR_NAME            = "author_name";
	public static final String COLUMN_AUTHOR_EMAIL_HASH      = "author_email_hash";
	public static final String COLUMN_TITLE                  = "title";
	public static final String COLUMN_BODY                   = "body";
	public static final String COLUMN_TAGS                   = "tags";  // this perhaps ought to be a separate table
	public static final String COLUMN_CREATION_DATE          = "creation_date";
	public static final String COLUMN_LAST_MODIFICATION_DATE = "last_modification_date";
	public static final String COLUMN_URL                    = "url";
	public static final String COLUMN_READ                   = "read";
	
	// Creates a ContentValues object from the provided JSON.
	public static ContentValues convertToContentValues(JSONObject article) throws JSONException {
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, article.getInt("id"));
		// TODO: it might be better to have the author data in a separate table.
		JSONObject author = article.getJSONObject("author");
		values.put(COLUMN_AUTHOR_NAME, author.getString("name"));
		values.put(COLUMN_AUTHOR_EMAIL_HASH, author.getString("email_hash"));
		values.put(COLUMN_TITLE, article.getString("title"));
		values.put(COLUMN_BODY, article.getString("body"));
		values.put(COLUMN_CREATION_DATE, article.getInt("date"));
		values.put(COLUMN_LAST_MODIFICATION_DATE, article.getInt("date"));
		values.put(COLUMN_URL, article.getString("url"));
		values.put(COLUMN_READ, false);
		return values;
	}
}
