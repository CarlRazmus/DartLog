package com.fraz.dartlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class AppSettingsActivity extends Activity {

    AppSettingsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragment = new AppSettingsFragment();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment)
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
