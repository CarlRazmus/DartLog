package com.fraz.dartlog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

public class PlayerSelectorDialog extends Dialog {

    public PlayerSelectorDialogListener mListener;


    public PlayerSelectorDialog(@NonNull Context context) {
        super(context);
    }


    public interface PlayerSelectorDialogListener {
        void onDialogPositiveClick(PlayerSelectorDialog dialog);
    }
}
