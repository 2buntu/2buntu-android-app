package com.twobuntu.twobuntu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

// This activity displays a list of articles on smaller devices and also article
// contents on devices with large enough screens (like tablets).
public class ArticleListActivity extends Activity implements
		ArticleListFragment.ArticleSelectedListener {

	// Whether we are displaying two panes or not.
	private boolean mTwoPane;

	// Initializes the activity.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_list);
		
		// Attempt to find the article content container - which will only
		// be present on devices with larger screens.
		if (findViewById(R.id.article_detail_container) != null) {
			mTwoPane = true;
			((ArticleListFragment) getFragmentManager()
					.findFragmentById(R.id.article_list))
					.setActivateOnItemClick(true);
		}
	}
	
	// Invoked when an individual article is selected.
	@Override
	public void onArticleSelected(int id) {
		if (mTwoPane) {
			// If we are displaying two panes, then simply replace the existing fragment.
			Bundle arguments = new Bundle();
			arguments.putInt(ArticleDetailFragment.ARG_ARTICLE_ID, id);
			ArticleDetailFragment fragment = new ArticleDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
					.replace(R.id.article_detail_container, fragment).commit();

		} else {
			// If we are only displaying one pane, create an intent to display the article.
			Intent detailIntent = new Intent(this, ArticleDetailActivity.class);
			detailIntent.putExtra(ArticleDetailFragment.ARG_ARTICLE_ID, id);
			startActivity(detailIntent);
		}
	}
}
