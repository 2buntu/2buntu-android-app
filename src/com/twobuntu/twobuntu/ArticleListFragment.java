package com.twobuntu.twobuntu;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

// Displays the list of articles on the home page.
public class ArticleListFragment extends ListFragment {

	// Used for storing the ID of the current article.
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	// The callback used for notification of article selection.
	private SelectionCallback mCallbacks = sDummyCallback;

	// The currently selected article.
	private int mActivatedPosition = ListView.INVALID_POSITION;

	// Interface for notification of article selection changes.
	public interface SelectionCallback {
		
		// An article is selected.
		public void onArticleSelected(int id);
	}

	// A "dummy" implementation of the SelectionCallback class. 
	private static SelectionCallback sDummyCallback = new SelectionCallback() {
		
		// Do nothing.
		@Override
		public void onArticleSelected(int id) {
		}
	};

	public ArticleListFragment() {
	}

	// The adapter used for retrieving article information.
	private ArticleAdapter mAdapter;
	
	// Begins loading the list of articles.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new ArticleAdapter(getActivity());
		// Set the list adapter.
		setListAdapter(mAdapter);
		mAdapter.refresh();
	}

	// Restores the previously selected article if possible.
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// Restore the previously serialized activated item position.
		if(savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION))
			setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Activities containing this fragment must implement its callbacks.
		if(!(activity instanceof SelectionCallback)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		mCallbacks = (SelectionCallback)activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallback;
	}

	// An item has been selected, so invoke the callback.
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		// TODO: replace 0 with the actual article ID.
		mCallbacks.onArticleSelected(0);
	}

	// Save the currently selected article.
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(mActivatedPosition != ListView.INVALID_POSITION)
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
	}

	// This causes list items to enter the activated state when clicked.
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		getListView().setChoiceMode(activateOnItemClick?
				ListView.CHOICE_MODE_SINGLE:
				ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if(position == ListView.INVALID_POSITION)
			getListView().setItemChecked(mActivatedPosition, false);
		else
			getListView().setItemChecked(position, true);
		mActivatedPosition = position;
	}
}
