package com.twobuntu.twobuntu;

import android.os.Bundle;
import android.preference.PreferenceFragment;

// Provides means of modifying application preferences.
public class SettingsFragment extends PreferenceFragment {
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}