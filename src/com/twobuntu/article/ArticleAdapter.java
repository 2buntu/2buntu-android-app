package com.twobuntu.article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.twobuntu.twobuntu.APIRequest;
import com.twobuntu.twobuntu.R;

public class ArticleAdapter extends ArrayAdapter<Article> {
	
	// Refreshes the list of articles.
	public void refresh() {
		// Make the API request for the list data.
		APIRequest.load("/articles", new JsonHttpResponseHandler() {
			
			@Override
			public void onFailure(Throwable e, String response) {
				// TODO: worst error handling ever.
			    System.out.println("Error! " + response);
			}
			
			@Override
			public void onSuccess(JSONObject response) {
				try {
					// Clear the current contents and insert the JSONObjects.
					clear();
					JSONArray articles = response.getJSONArray("articles");
					for(int i=0; i<articles.length(); ++i)
						add(new Article(articles.getJSONObject(i)));
				} catch (JSONException e) {
					// TODO: print a better error message.
					System.out.println("JSON error of some sort.");
				}
			}
		});
	}
	
	// Initializes the adapter.
	public ArticleAdapter(Context context) {
		super(context, R.layout.article_list_item, R.id.article_list_item_title);
	}
	
	// Displays an individual article in the list.
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = super.getView(position, convertView, parent);
		Article article = getItem(position);
		((TextView)convertView.findViewById(R.id.article_list_item_title)).setText(article.title);
		return convertView;
	}
}
