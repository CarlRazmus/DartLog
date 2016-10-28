package com.fraz.dartlog.game.x01;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.fraz.dartlog.R;

public class X01SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.x01_preferences);
    }
}
