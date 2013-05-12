package com.twobuntu.article;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Article {
	
	// Information for a specific article.
	public int id;
	public String title;
	public Author author;
	public ArrayList<String> tags;
	
	// Initializes the article with the specified JSON object.
	public Article(JSONObject data) throws JSONException {
		id     = data.getInt("id");
		title  = data.getString("title");
		author = new Author(data.getJSONObject("author"));
		tags   = new ArrayList<String>();
		// Copy the tags into the array.
		JSONArray json_tags = data.getJSONArray("tags");
		for(int i=0; i<json_tags.length();++i)
			tags.add(json_tags.getString(i));
	}
}