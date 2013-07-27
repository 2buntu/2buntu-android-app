package com.twobuntu.twobuntu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class ArticleCommentsActivity extends Activity {
	
	// Initializes the activity.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_comments);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// Recreate the fragment if the saved instance state is empty.
		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putLong(ArticleCommentsFragment.ARG_ARTICLE_ID,
					getIntent().getLongExtra(ArticleCommentsFragment.ARG_ARTICLE_ID, 0L));
			ArticleCommentsFragment fragment = new ArticleCommentsFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
					.add(R.id.article_comments_container, fragment).commit();
		}
	}

	// If the user navigates up, then return to the article list page.
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		    case android.R.id.home:
		    	Intent intent = new Intent(this, ArticleDetailActivity.class);
		    	intent.putExtra(ArticleCommentsFragment.ARG_ARTICLE_ID,
						getIntent().getLongExtra(ArticleCommentsFragment.ARG_ARTICLE_ID, 0L));
			    NavUtils.navigateUpTo(this, intent);
			    return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
