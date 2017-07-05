package com.fraz.dartlog;

import android.content.Intent;
import android.os.Bundle;

public class AppSettingsActivity extends MenuBackground {

    AppSettingsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this, R.layout.activity_app_settings);

        fragment = new AppSettingsFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.app_preferences, fragment)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == DbFileHandler.WRITE_REQUEST_CODE)
                fragment.onSuccessfulCreateDb(data);
            if (requestCode == DbFileHandler.OPEN_REQUEST_CODE)
                fragment.onSuccessfulOpenDb(data);
        }
    }
}
