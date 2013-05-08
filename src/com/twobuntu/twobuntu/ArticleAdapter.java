package com.twobuntu.twobuntu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

public class ArticleAdapter extends ArrayAdapter<JSONObject> {
	
	// Refreshes the list of articles.
	public void refresh() {
		// Make the API request for the list data.
		APIRequest.load("/articles", new JsonHttpResponseHandler() {
			
			@Override
			public void onFailure(Throwable e, String response) {
			    System.out.println("Error! " + response);
			}
			
			@Override
			public void onSuccess(JSONObject response) {
				try {
					// Clear the current contents and insert the JSONObjects.
					clear();
					JSONArray articles = response.getJSONArray("articles");
					for(int i=0; i<articles.length(); ++i)
						add(articles.getJSONObject(i));
				} catch (JSONException e) {
					// TODO: print some sort of error.
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
		JSONObject item = getItem(position);
		try {
			((TextView)convertView.findViewById(R.id.article_list_item_title)).setText(item.getString("title"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}
		return convertView;
	}
}
