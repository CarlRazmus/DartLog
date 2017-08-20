package com.fraz.dartlog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

public class AppSettingsActivity extends MenuBackground {

    AppSettingsFragment fragment;

    private OnBackPressedDialogFragment.OnBackPressedDialogListener onBackPressedDialogListener;

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
                if(DbFileHandler.isDbExtension(intent.getData(), parentActivity))
                    fragment.onSuccessfullyCreatedExternalDbFile(intent);
                else {
                    fragment.onUnsuccessfulExternalDbFileVerification(intent);
                    openErrorOccurredDialog(intent);
                }
            }
            if (requestCode == DbFileHandler.OPEN_REQUEST_CODE)
                if(DbFileHandler.verifyImportedDb(intent.getData(), parentActivity)){
                    fragment.onSuccessfulFindExistingExternalDb(intent);
                }
        }
    }

    private void openErrorOccurredDialog(final Intent intent){
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_message_missing_file_extension)
                .setTitle(R.string.dialog_title_missing_file_extension)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        fragment.onUnsuccessfulExternalDbCreation(intent);
                    }
                });
        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
