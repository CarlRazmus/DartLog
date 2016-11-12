package com.fraz.dartlog.game.random;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.fraz.dartlog.R;

/**
 * Created by CarlR on 03/11/2016.
 */
public class RandomSettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.random_preferences);
    }
}
