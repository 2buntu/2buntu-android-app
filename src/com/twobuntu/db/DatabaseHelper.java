package com.twobuntu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Initializes the database and provides means of accessing it.
public class DatabaseHelper extends SQLiteOpenHelper {

	// The name and version of the database.
	private static final String DATABASE_NAME = "twobuntu";
	private static final int DATABASE_VERSION = 1;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	// Creates the tables in the database (only one currently).
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Articles.TABLE_NAME + " (" +
				Articles.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
				Articles.COLUMN_AUTHOR_NAME + " VARCHAR(100)," +
				Articles.COLUMN_AUTHOR_EMAIL_HASH + " VARCHAR(32)," +
				Articles.COLUMN_TITLE + " VARCHAR(200)," +
				Articles.COLUMN_BODY + " TEXT," +
				Articles.COLUMN_TAGS + " VARCHAR(100)," +
				Articles.COLUMN_CREATION_DATE + " INTEGER," +
				Articles.COLUMN_LAST_MODIFICATION_DATE + " INTEGER);");
	}

	// Updates the tables in the database (for example, after upgrading the app).
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// We are on the first version, so do nothing.
	}
}
