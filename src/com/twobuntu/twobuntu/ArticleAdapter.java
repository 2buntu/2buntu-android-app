package com.twobuntu.twobuntu;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.loopj.android.http.JsonHttpResponseHandler;

public class ArticleAdapter extends ArrayAdapter<JSONObject> {
	
	// Refreshes the list of articles.
	private void refresh() {
		// Make the API request for the list data.
		APIRequest.load("/articles", new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(JSONObject response) {
				/*
				JSONArray articles = response.getJSONArray("articles");
				// Clear the current contents and insert the JSONObjects.
				clear();
				for(int i=0; i<articles.length(); ++i)
					add(articles.getJSONObject(i));
			    */
			}
		});
	}
	
	// Initializes the adapter.
	public ArticleAdapter(Context context) {
		super(context, R.layout.article_list_item);
	}
}
