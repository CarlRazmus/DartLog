package com.fraz.dartlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class AppSettingsFragment extends PreferenceFragment {

    DbFileHandler dbFileHandler;
    Activity parent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parent = getActivity();
        dbFileHandler = new DbFileHandler(parent);

        addPreferencesFromResource(R.xml.app_preferences);

        Preference export_db_preference = findPreference(getString(R.string.export_database));
        export_db_preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                dbFileHandler.createCopyOfLocalDb();
                return true;
            }
        });

        Preference import_db_preference = findPreference(getString(R.string.import_database));
        import_db_preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                dbFileHandler.selectExternalDbFile();
                return true;
            }
        });
    }

    public void onSuccessfulCreateDb(Intent data) {
        dbFileHandler.onFileCreated(data.getData());
    }

    public void onSuccessfulOpenDb(Intent data) {
        dbFileHandler.onFileSelected(data.getData());
    }
}