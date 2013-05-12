package com.twobuntu.article;

import org.json.JSONException;
import org.json.JSONObject;

//Represents an article's author.
public class Author {
	
	// Information about the author.
	public String email_hash;
	public String name;
	
	// Initializes the author with the specified JSON object.
	public Author(JSONObject data) throws JSONException {
		email_hash = data.getString("email_hash");
		name       = data.getString("name");
	}
	
	// Utility method for fetching Gravatar.
	public String getGravatar(int size) {
		return "http://gravatar.com/avatar/" + email_hash + "?s=" + size + "&d=identicon";
	}
}