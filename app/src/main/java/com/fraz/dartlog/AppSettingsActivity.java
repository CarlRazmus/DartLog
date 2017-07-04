package com.fraz.dartlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AppSettingsActivity extends Activity {

    AppSettingsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragment = new AppSettingsFragment();

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == DbFileHandler.WRITE_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d("dbFileHandler", "URI = " + data.getData());
            Log.d("dbFileHandler", "getPath = " + data.getData().getPath());
            fragment.onSuccessfulCreateDb(data);
        }
    }
}
