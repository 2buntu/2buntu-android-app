package com.twobuntu.twobuntu;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.twobuntu.db.Article;
import com.twobuntu.db.ArticleProvider;

// Displays details about the particular article.
public class ArticleDetailFragment extends Fragment {
	
	// This is the ID of the article being displayed.
	public static final String ARG_ARTICLE_ID = "article_id";
	
	// This holds the ID and URL of the current article.
	private long mID;
	private String mURL;
	
	// Columns to retrieve for the article.
	private static final String[] mColumns = new String[] {
		Article.COLUMN_TITLE,
		Article.COLUMN_AUTHOR_EMAIL_HASH,
		Article.COLUMN_CREATION_DATE,
		Article.COLUMN_AUTHOR_NAME,
		Article.COLUMN_BODY,
		Article.COLUMN_URL
	};
	
	// Generates the HTML for the entire page given the title and body to display.
	@SuppressLint("SimpleDateFormat")
	private String generateHTML(String title, String email_hash, long creation_date, String author, String body) {
		String date = new SimpleDateFormat("MMMM d, yyyy").format(new Date(creation_date * 1000));
		return "<html>" +
	           "<head>" +
	           "  <link rel='stylesheet' href='css/style.css'>" +
	           "</head>" +
	           "<body>" +
	             "<header>" +
	               "<div>" +
	                 "<img src='http://gravatar.com/avatar/" + email_hash + "?s=64&d=identicon'>" +
	                 "<h2>" + title + "</h2>" +
	               "</div>" +
	               "<table><tr><td>by " + author + "</td>" +
	               "<td>" + date + "</td></tr></table>" +
	             "</header>" +
	             body +
	           "</body>" +
	           "</html>";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Indicate that this fragment has a context menu and should not be recreated on a config change.
		setHasOptionsMenu(true);
		setRetainInstance(true);
		// Attempt to load the specified article.
		View rootView = inflater.inflate(R.layout.fragment_article_detail, container, false);
		if(getArguments().containsKey(ARG_ARTICLE_ID)) {
		    Uri uri = Uri.withAppendedPath(ArticleProvider.CONTENT_LOOKUP_URI,
		    		String.valueOf(getArguments().getLong(ARG_ARTICLE_ID)));
		    Cursor cursor = getActivity().getContentResolver().query(uri, mColumns, null, null, null);
		    cursor.moveToFirst();
		    // Set the title and body.
			WebView webView = (WebView)rootView.findViewById(R.id.article_content);
			webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
			webView.loadDataWithBaseURL("file:///android_asset/", generateHTML(cursor.getString(0),
			        cursor.getString(1), cursor.getLong(2), cursor.getString(3), cursor.getString(4)),
			        "text/html", "utf-8", null);
			// ...and remember the ID + URL.
			mID  = getArguments().getLong(ARG_ARTICLE_ID);
			mURL = cursor.getString(5);
		}
		return rootView;
	}
	
	// Add the "toolbar" buttons to the action bar.
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.activity_article_detail, menu);
	}
	
	// Process an item from the action bar.
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		    case R.id.menu_comments:
		    {
		    	Intent intent = new Intent(getActivity(), ArticleCommentsActivity.class);
		    	intent.putExtra(ArticleCommentsFragment.ARG_ARTICLE_ID, mID);
		    	startActivity(intent);
		    	return true;
		    }
		    case R.id.menu_share:
		    {
		    	Intent intent = new Intent(Intent.ACTION_SEND);
		    	intent.setType("text/plain");
		    	intent.putExtra(android.content.Intent.EXTRA_TEXT, mURL);
		    	getActivity().startActivity(Intent.createChooser(intent,
		    			getActivity().getResources().getText(R.string.intent_share_article)));
		    	return true;
		    }
		    default:
		    	return false;
		}
	}
}
