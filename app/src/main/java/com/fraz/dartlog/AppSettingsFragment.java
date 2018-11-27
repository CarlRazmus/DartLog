package com.fraz.dartlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.Gravity;
import android.widget.Toast;

import com.fraz.dartlog.db.DartLogDatabaseHelper;

public class AppSettingsFragment extends PreferenceFragment {

    DbFileHandler dbFileHandler;
    Activity parent;

    private void showToast(int resId) {
        Toast toast = Toast.makeText(parent, resId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

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

    public void onSuccessfullyCreatedExternalDbFile(Intent intent) {
        dbFileHandler.copyLocalDbDataToExternalFile(intent.getData());
        showToast(R.string.successful_export);
    }

    public void onSuccessfulFindExistingExternalDb(Intent intent) {
        dbFileHandler.copyDataFromExternalFileToLocalDb(intent.getData());
        DartLogDatabaseHelper dbHelper = DartLogDatabaseHelper.getInstance(parent);
        Util.saveProfileNames(dbHelper.getPlayers(), parent);
        showToast(R.string.successful_import);
    }

    public void onUnsuccessfulExternalDbCreation(Intent intent){
        /* TODO: Should add missing .db extension or remove the file */
        showToast(R.string.unsuccessful_export);
    }

    public void onUnsuccessfulImportOfDatabase(Intent intent) {
        /* TODO: Should add missing .db extension or remove the file? */
        showToast(R.string.unsuccessful_import);
    }

    public void onUnsuccessfulExportOfDatabase(Intent intent) {
        /* TODO: Should add missing .db extension or remove the file? */
        showToast(R.string.unsuccessful_export);
    }


}