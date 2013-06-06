package com.twobuntu.article;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.twobuntu.twobuntu.R;

public class ArticleAdapter extends ArrayAdapter<Article> {
	
	// Interface for providing notification when the refresh process has completed.
	public interface RefreshListener {
		public void onRefresh();
	}
	
	// Pointer to the current listener.
	private RefreshListener mListener;
	
	// Initializes the adapter.
	public ArticleAdapter(Context context) {
		super(context, R.layout.article_list_item, R.id.article_list_item_title);
	}
	
	// Displays an individual article in the list.
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = super.getView(position, convertView, parent);
		Article article = getItem(position);
		((TextView)convertView.findViewById(R.id.article_list_item_title)).setText(article.mTitle);
		// TODO: load and display the Gravatars.
		return convertView;
	}
	
	// Refreshes the list of articles.
	public void refresh(Context context) {
		// Make the API request for the list data.
		APIRequest.load("/articles", new APIRequest.ResponseCallback() {
			
			@Override
			public void onFailure(String response) {
				// TODO: worst error handling ever.
			    System.out.println("Error: " + response);
			}
			
			@Override
			public void onSuccess(JSONArray items) throws JSONException {
				// Clear the current contents and insert the JSONObjects.
				clear();
				for(int i=0; i<items.length(); ++i)
					add(new Article(items.getJSONObject(i)));
				// Invoke the refresh listener.
				if(mListener != null)
					mListener.onRefresh();
			}
		});
	}
	
	public void setOnRefreshListener(RefreshListener listener) {
		mListener = listener;
	}
}
