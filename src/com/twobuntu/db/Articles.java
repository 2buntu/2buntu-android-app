package com.twobuntu.db;

// Represents a means of interacting with the articles table.
public class Articles {
	
	// The name of the articles table.
	public static final String TABLE_NAME = "article";
	
	// The columns in the article table.
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_AUTHOR_NAME = "author_name";
	public static final String COLUMN_AUTHOR_EMAIL_HASH = "author_email_hash";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_BODY = "body";
	public static final String COLUMN_TAGS = "tags";  // this perhaps ought to be a separate table
	public static final String COLUMN_CREATION_DATE = "creation_date";
	public static final String COLUMN_LAST_MODIFICATION_DATE = "last_modification_date";
}
