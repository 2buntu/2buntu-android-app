package com.twobuntu.twobuntu;

import android.app.Activity;
import android.os.Bundle;

// Acts as a container for SettingsFragment.
public class SettingsActivity extends Activity {
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
