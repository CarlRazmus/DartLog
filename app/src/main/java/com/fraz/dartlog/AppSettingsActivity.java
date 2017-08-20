package com.fraz.dartlog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(resultCode == RESULT_OK) {
            if (requestCode == DbFileHandler.WRITE_REQUEST_CODE) {
                if(DbFileHandler.isDbExtension(intent.getData()))
                    fragment.onSuccessfullyCreatedExternalDbFile(intent);
                else {
                    fragment.onUnsuccessfulExternalDbFileVerification(intent);
                    openErrorOccurredDialog();
                }
            }
            if (requestCode == DbFileHandler.OPEN_REQUEST_CODE)
                if(DbFileHandler.isDbExtension(intent.getData()))
                    fragment.onSuccessfulFindExistingExternalDb(intent);
        }
    }

    private void openErrorOccurredDialog(){
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_message_database_not_created)
                .setTitle(R.string.dialog_title_database_not_created);

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
