package com.twobuntu.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.twobuntu.twobuntu.R;

public class MainActivity extends Activity {
	
	DrawerLayout mDrawerLayout;
	ActionBarDrawerToggle mDrawerToggle;
	ListView mDrawerList;
	
	private class DrawerItem {
		
		int mTitleRes;
		int mIconRes;
		Class<?> mFragmentClass;
		
		DrawerItem(int titleRes, int iconRes, Class<?> fragmentClass) {
			mTitleRes      = titleRes;
			mIconRes       = iconRes;
			mFragmentClass = fragmentClass;
		}
	}

	DrawerItem[] mDrawerItems = new DrawerItem[] {
			new DrawerItem(R.string.drawer_item_articles, R.drawable.ic_nav_articles, ArticleListFragment.class),
			new DrawerItem(R.string.drawer_item_authors,  R.drawable.ic_nav_authors,  AuthorListFragment.class),
			new DrawerItem(R.string.drawer_item_search,   R.drawable.ic_nav_search,   SearchFragment.class),
			new DrawerItem(R.string.drawer_item_settings, R.drawable.ic_nav_settings, SettingsFragment.class)
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
		
		mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        mDrawerToggle = new ActionBarDrawerToggle(
        		this,
        		mDrawerLayout,
        		R.drawable.ic_drawer,
        		R.string.drawer_open,
        		R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		mDrawerList = (ListView)findViewById(R.id.left_drawer);
		mDrawerList.setAdapter(new ArrayAdapter<DrawerItem>(
				this,
				android.R.layout.simple_list_item_activated_1,
				mDrawerItems) {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				convertView = super.getView(position, convertView, parent);
				TextView textView = (TextView)convertView.findViewById(android.R.id.text1);
				textView.setText(mDrawerItems[position].mTitleRes);
				textView.setCompoundDrawablesWithIntrinsicBounds(
						mDrawerItems[position].mIconRes,
						0,
						0,
						0);
				textView.setCompoundDrawablePadding(20);
				return convertView;
			}
		});
		mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectItem(position);
			}
		});
		
        if (savedInstanceState == null)
            selectItem(0);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	private void selectItem(int position) {
		try {
			Fragment fragment = (Fragment)mDrawerItems[position].mFragmentClass.newInstance();
			
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
			
			mDrawerList.setItemChecked(position, true);
	        mDrawerLayout.closeDrawer(mDrawerList);
		} catch (Exception e) {
			// Print a stack trace but ignore the exception
			e.printStackTrace();
		}
	}
}
