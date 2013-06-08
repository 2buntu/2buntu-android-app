package com.twobuntu.twobuntu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

// Acts as a container for the ArticleDetailFragment.
public class ArticleDetailActivity extends Activity {

	// Initializes the activity.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_detail);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// Recreate the fragment if the saved instance state is empty.
		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putLong(ArticleDetailFragment.ARG_ARTICLE_ID,
					getIntent().getLongExtra(ArticleDetailFragment.ARG_ARTICLE_ID, 0));
			ArticleDetailFragment fragment = new ArticleDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
					.add(R.id.article_detail_container, fragment).commit();
		}
	}

	// If the user navigates up, then return to the article list page.
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		    case android.R.id.home:
			    NavUtils.navigateUpTo(this, new Intent(this, ArticleListActivity.class));
			    return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
