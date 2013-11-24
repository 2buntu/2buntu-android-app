package com.twobuntu.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.twobuntu.twobuntu.R;

public class MainActivity extends FragmentActivity {
	
	ActionBarDrawerToggle mDrawerToggle;
	
	private class DrawerItem {
		
		int mTitleRes;
		int mIconRes;
		
		DrawerItem(int titleRes, int iconRes) {
			mTitleRes = titleRes;
			mIconRes  = iconRes;
		}
	}

	DrawerItem[] mDrawerItems = new DrawerItem[] {
			new DrawerItem(R.string.drawer_item_articles, R.drawable.ic_nav_articles),
			new DrawerItem(R.string.drawer_item_authors,  R.drawable.ic_nav_authors),
			new DrawerItem(R.string.drawer_item_search,   R.drawable.ic_nav_search),
			new DrawerItem(R.string.drawer_item_settings, R.drawable.ic_nav_settings)
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		ListView drawerList = (ListView)findViewById(R.id.left_drawer);
		
		drawerList.setAdapter(new ArrayAdapter<DrawerItem>(
				this,
				android.R.layout.simple_list_item_1,
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
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        mDrawerToggle = new ActionBarDrawerToggle(
        		this,
        		drawerLayout,
        		R.drawable.ic_drawer,
        		R.string.drawer_open,
        		R.string.drawer_close);
        drawerLayout.setDrawerListener(mDrawerToggle);
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
}
