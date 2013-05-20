package com.twobuntu.article;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

// Represents an individual article.
public class Article {
	
	// Interface for notification of the article body loading.
	public interface BodyLoadedCallback {
		public void onBodyLoaded();
	}
	
	// Information for a specific article.
	public int mId;
	public String mTitle;
	public Author mAuthor;
	public ArrayList<String> mTags;
	public String mBody;
	
	// Initializes the article with the specified JSON data.
	public Article(JSONObject data) throws JSONException {
		setData(data);
	}
	
	// Initializes the article with the specified ID.
	public Article(final Context context, int id, final BodyLoadedCallback callback) {
		APIRequest.load(context, "/articles/" + id, new APIRequest.ResponseCallback() {
			
			// Display an error message on failure.
			@Override
			public void onFailure(String error) {
				Toast.makeText(context, error, Toast.LENGTH_LONG).show();
			}
			
			// Set the data for the article and inform the caller.
			@Override
			public void onSuccess(JSONArray items) throws JSONException {
				setData(items.getJSONObject(0));
				callback.onBodyLoaded();
			}
		});
	}
	
	// Sets the article data to the specified JSON array.
	public void setData(JSONObject data) throws JSONException {
		mId     = data.getInt("id");
		mTitle  = data.getString("title");
		mAuthor = new Author(data.getJSONObject("author"));
		mTags   = new ArrayList<String>();
		// Copy the tags into the array.
		JSONArray json_tags = data.getJSONArray("tags");
		for(int i=0; i<json_tags.length();++i)
			mTags.add(json_tags.getString(i));
		// Add the body if present.
		if(data.has("body"))
			mBody = data.getString("body");
	}
}