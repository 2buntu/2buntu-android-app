package com.twobuntu.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.twobuntu.twobuntu.R;

public class SettingsFragment extends PreferenceFragment {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}