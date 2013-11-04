package com.twobuntu.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.twobuntu.twobuntu.R;

public class ArticleListActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_list);
	}
}
