package com.twobuntu.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

// Provides access to the articles.
public class ArticleProvider extends ContentProvider {

	// The authority for the provider.
	private static final String AUTHORITY = "com.twobuntu.articleprovider";
	
	// This URI is public and intended to be used by other parts of the application.
	public static final Uri ARTICLES_URI = Uri.parse("content://" + AUTHORITY + "/articles");
	
	// Codes that represent individual URIs.
	private static final int ARTICLES = 1;
	private static final int ARTICLE = 2;
	
	// Used for matching URIs.
	private static UriMatcher mMatcher;
	
	// Initialize the URIs for articles.
	static {
		mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		mMatcher.addURI(AUTHORITY, "articles", ARTICLES);
		mMatcher.addURI(AUTHORITY, "article/#", ARTICLE);
	}
	
	// Provides access to the database.
	private ArticleHelper mHelper;
	
	@Override
	public boolean onCreate() {
		mHelper = new ArticleHelper(getContext());
		return true;
	}
	
	// Returns the type returned by the specified URI.
	@Override
	public String getType(Uri uri) {
		switch(mMatcher.match(uri)) {
		    case ARTICLES:
		    	return "vnd.android.cursor.dir/vnd." + AUTHORITY + ".articles";
		    case ARTICLE:
		    	return "vnd.android.cursor.item/vnd." + AUTHORITY + ".articles";
		    default:
		    	throw new IllegalArgumentException();
		}
	}
	
	// Returns a cursor for the provided query.
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		switch(mMatcher.match(uri)) {
		    case ARTICLES:
			    break;
		    case ARTICLE:
			    selection += Articles.COLUMN_ID + " = " + uri.getLastPathSegment();
			    break;
			default:
				throw new IllegalArgumentException();
		}
		// Return the cursor to the user.
		return mHelper.getReadableDatabase().query(Articles.TABLE_NAME, projection,
				selection, selectionArgs, null, null, sortOrder);
	}
	
	// Inserts an article into the database.
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if(mMatcher.match(uri) != ARTICLES)
			throw new IllegalArgumentException();
		// Perform the actual insert operation.
		long id = mHelper.getWritableDatabase().insert(Articles.TABLE_NAME, null, values);
		// Generate the URI of the new article.
		Uri articleUri = Uri.parse("content://" + AUTHORITY + "/article/" + id);
		getContext().getContentResolver().notifyChange(articleUri, null);
		return articleUri;
	}

	// Updates an article in the database.
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		//...
		getContext().getContentResolver().notifyChange(uri, null);
		return 0;
	}
	
	// As there is currently no need to delete articles, this is unimplemented.
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}
}
