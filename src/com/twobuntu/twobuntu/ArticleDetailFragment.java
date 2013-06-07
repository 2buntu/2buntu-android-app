package com.twobuntu.twobuntu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

// Displays details about the particular article.
public class ArticleDetailFragment extends Fragment {
	
	// This is the ID of the article being displayed.
	public static final String ARG_ARTICLE_ID = "article_id";

	public ArticleDetailFragment() {
	}
	
	// The currently displayed article.
	//private Article mArticle;
	
	// Begins loading the article and preparing it for display.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		// If the article ID was provided, then load it.
		if (getArguments().containsKey(ARG_ARTICLE_ID)) {
			mArticle = new Article(getActivity(), getArguments().getInt(ARG_ARTICLE_ID),
					new BodyLoadedCallback() {
				
				// Sets the webview's contents to that of the article's body.
				@Override
				public void onBodyLoaded() {
					getActivity().setTitle(mArticle.mTitle);
					WebView webView = (WebView)getActivity().findViewById(R.id.article_content);
					webView.loadData(mArticle.mBody, "text/html", "utf-8");
				}
			});
		}
		*/
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_article_detail, container, false);
		// TODO
		return rootView;
	}
}
