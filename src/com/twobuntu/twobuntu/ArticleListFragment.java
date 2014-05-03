package com.twobuntu.twobuntu;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.twobuntu.db.Article;
import com.twobuntu.db.ArticleProvider;

// Displays the list of articles on the home page.
public class ArticleListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

	// Used for storing the ID of the current article.
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	
	// Columns to retrieve and ...
	private static final String[] mColumns = new String[] {
		Article.COLUMN_ID,
		Article.COLUMN_AUTHOR_EMAIL_HASH,
		Article.COLUMN_TITLE,
		Article.COLUMN_AUTHOR_NAME
	};
	
	private static final int[] mViews = new int[] {
		0,
		android.R.id.icon,
		android.R.id.text1,
		android.R.id.text2
	};
	
	// Interface for providing notification of list item selections.
	public interface ArticleSelectedListener {
		void onArticleSelected(long id);
	}
	
	// The current listener for article selection events.
	private ArticleSelectedListener mListener;
	
	// The currently selected article.
	private int mCurrentArticle = ListView.INVALID_POSITION;

	// The adapter used for retrieving article information.
	private SimpleCursorAdapter mAdapter;
	
	// The custom typeface we are using for displaying the article list.
	private Typeface mTypeface;
	
	// Begin loading the articles.
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Set the Ubuntu font.
		mTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Ubuntu.ttf");
		// This fragment has a menu and should be retained during configuration changes.
		setHasOptionsMenu(true);
		setRetainInstance(true);
		// Initialize the cursor adapter.
		mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.listview_text_with_icon,
				null, mColumns, mViews, 0) {
			
			// We need to do this in order to customize the typeface.
		    @Override
		    public View getView(int position, View convertView, ViewGroup parent) {
		        convertView = super.getView(position, convertView, parent);
		        ((TextView)convertView.findViewById(android.R.id.text1)).setTypeface(mTypeface);
		        ((TextView)convertView.findViewById(android.R.id.text2)).setTypeface(mTypeface);
		        return convertView;
		    }
		};
		// Instruct the adapter to load the images asynchronously.
		mAdapter.setViewBinder(new ViewBinder() {
			
			@SuppressWarnings("deprecation")
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				if(view.getId() == android.R.id.icon) {
					ImageView imageView = (ImageView)view.findViewById(android.R.id.icon);
					String url = "http://gravatar.com/avatar/" +
					        cursor.getString(1) + "?s=48&d=identicon";
					DisplayImageOptions options = new DisplayImageOptions.Builder()
			                .showStubImage(R.drawable.ic_gravatar_stub)
			                .cacheInMemory(true)
			                .build();
					ImageLoader.getInstance().displayImage(url, imageView, options);
					return true;
				}
				return false;
			}
		});
		// Set the adapter and begin loading the data.
		setListAdapter(mAdapter);
		setListShown(false);
		getLoaderManager().initLoader(0, null, this);
	}

	// Restores the previously selected article if possible.
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// Restore the previously serialized activated item position.
		if(savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION))
			setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
	}

	// Grab a pointer to the listener for article selection events.
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(!(activity instanceof ArticleSelectedListener))
			throw new IllegalStateException("Activity must implement ArticleSelectedListener.");
		mListener = (ArticleSelectedListener)activity;
	}
	
	// Add the "toolbar" buttons to the action bar.
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.activity_article_list, menu);
	}
	
	// Process an item from the action bar.
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		    case R.id.menu_settings:
		    	startActivity(new Intent(getActivity(), SettingsActivity.class));
		    	return true;
		    default:
		        return false;
		}
	}

	// When the activity is destroyed, remove the callback.
	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	// Process click events for the list view..
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		if(mListener != null)
		    mListener.onArticleSelected(id);
	}

	// Save the currently selected article.
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(mCurrentArticle != ListView.INVALID_POSITION)
			outState.putInt(STATE_ACTIVATED_POSITION, mCurrentArticle);
	}

	// This causes list items to enter the activated state when clicked.
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		getListView().setChoiceMode(activateOnItemClick?
				ListView.CHOICE_MODE_SINGLE:
				ListView.CHOICE_MODE_NONE);
	}

	// TODO: figure out if this is needed.
	private void setActivatedPosition(int position) {
		if(position == ListView.INVALID_POSITION)
			getListView().setItemChecked(mCurrentArticle, false);
		else
			getListView().setItemChecked(position, true);
		mCurrentArticle = position;
	}

	// Creates the loader.
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// Create the query used by the adapter.
		return new CursorLoader(getActivity(), ArticleProvider.CONTENT_URI, 
				mColumns, null, null, Article.COLUMN_CREATION_DATE + " DESC");
	}

	// Called when the loader finishes.
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mAdapter.swapCursor(data);
		if(data.getCount() != 0)
		    setListShown(true);
	}

	// When the loader is reset, simply insert a null cursor.
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}
}
