package com.fraz.dartlog;


import android.os.Bundle;
import android.preference.PreferenceFragment;

public class AppSettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.app_preferences);
    }
}