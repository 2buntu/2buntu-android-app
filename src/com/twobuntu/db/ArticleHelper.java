package com.twobuntu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Initializes the database and provides means of accessing it.
public class ArticleHelper extends SQLiteOpenHelper {

	// The name and version of the database.
	private static final String DATABASE_NAME = "twobuntu";
	private static final int DATABASE_VERSION = 2;
	
	public ArticleHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	// Creates the tables in the database (only one currently).
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Article.TABLE_NAME + " (" +
				Article.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
				Article.COLUMN_AUTHOR_NAME + " VARCHAR(100)," +
				Article.COLUMN_AUTHOR_EMAIL_HASH + " VARCHAR(32)," +
				Article.COLUMN_TITLE + " VARCHAR(200)," +
				Article.COLUMN_BODY + " TEXT," +
				Article.COLUMN_TAGS + " VARCHAR(100)," +
				Article.COLUMN_CREATION_DATE + " INTEGER," +
				Article.COLUMN_LAST_MODIFICATION_DATE + " INTEGER," +
				Article.COLUMN_URL + " VARCHAR(200)," +
				Article.COLUMN_READ + " BOOLEAN DEFAULT 0);");
	}

	// Updates the tables in the database (for example, after upgrading the app).
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion < 2)
			db.execSQL("ALTER TABLE " + Article.TABLE_NAME + " ADD COLUMN " +
		            Article.COLUMN_URL + " VARCHAR(200) DEFAULT \"http://2buntu.com\";");
		if(oldVersion < 3)
			db.execSQL("ALTER TABLE " + Article.TABLE_NAME + " ADD COLUMN " +
		            Article.COLUMN_READ + " BOOLEAN DEFAULT 0;");
	}
}
