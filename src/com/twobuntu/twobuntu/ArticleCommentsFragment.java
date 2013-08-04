package com.twobuntu.twobuntu;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.twobuntu.db.ArticleProvider;
import com.twobuntu.db.Article;

// Displays comments for an article.
public class ArticleCommentsFragment extends Fragment {
	
	// This is the ID of the article being displayed.
	public static final String ARG_ARTICLE_ID = "article_id";
	
	// Generates the HTML for the page given the title and URL.
	private String generateHTML(String title, String url) {
		return "<html>" +
		       "<head>" +
		       "  <link rel='stylesheet' href='css/style.css'>" +
		       "</head>" +
		       "<body>" +
		         "<header>" +
		           "<h2>Comments for \"" + title + "\"</h2>" +
		         "</header>" +
		         "<div id='disqus_thread'></div>" +
				 "<script type='text/javascript'>" +
				     "var disqus_shortname = '2buntu';" +
				     " var disqus_url = '" + url + "';" +
				     "(function() {" +
				     "var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;" +
				     "dsq.src = 'http://' + disqus_shortname + '.disqus.com/embed.js';" +
				     "(document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);" +
				     "})();" +
				 "</script>" +
				 "<a href='http://disqus.com' class='dsq-brlink'>loading <span class='logo-disqus'>Disqus</span> comments...</a>" +
		       "</body>" +
		       "</html>";
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Do not recreate on config change.
		setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.fragment_article_comments, container, false);
		if(getArguments().containsKey(ARG_ARTICLE_ID)) {
			// Load the title and URL of the article.
			Uri uri = Uri.withAppendedPath(ArticleProvider.CONTENT_LOOKUP_URI,
		    		String.valueOf(getArguments().getLong(ARG_ARTICLE_ID)));
			Cursor cursor = getActivity().getContentResolver().query(uri,
		    		new String[] { Article.COLUMN_TITLE, Article.COLUMN_URL },
		    		null, null, null);
			cursor.moveToFirst();
			// Display the comments.
			WebView webView = (WebView)rootView.findViewById(R.id.article_comments);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.setWebViewClient(new WebViewClient());
			webView.loadDataWithBaseURL("file:///android_asset/", generateHTML(cursor.getString(0),
			        cursor.getString(1)), "text/html", "utf-8", null);
		}
		return rootView;
	}
}
