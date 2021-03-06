package com.fraz.dartlog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

public class OnBackPressedDialogFragment extends DialogFragment {


    public interface OnBackPressedDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);

        void onDialogNegativeClick(DialogFragment dialog);
    }

    private OnBackPressedDialogListener onBackPressedDialogListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onBackPressedDialogListener = (OnBackPressedDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnBackPressedDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                R.style.RedButtonAlertDialog);
        builder.setMessage("Do you really want to end game? Current game will be lost.")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onBackPressedDialogListener.onDialogPositiveClick(
                                OnBackPressedDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onBackPressedDialogListener.onDialogNegativeClick(
                                OnBackPressedDialogFragment.this);
                    }
                });

        return builder.create();
    }
}
