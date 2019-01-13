package com.fraz.dartlog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;

public class PlayerSelectorDialogFragment extends DialogFragment{


    public interface PlayerSelectorDialogListener {
        void onDialogPositiveClick(PlayerSelectorDialogFragment dialog);
    }


    public PlayerSelectorDialogListener mListener;

    private ArrayList<String> playerNames = new ArrayList<>();
    private ArrayList<String> mSelectedItems = new ArrayList<>();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (PlayerSelectorDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.getClass().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.header_select_players)
                .setCancelable(true)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onDialogPositiveClick(PlayerSelectorDialogFragment.this);
                    }
                });

        updatePlayersFromDb();

        String[] namesArr = new String[playerNames.size()];
        for(int i = 0; i < playerNames.size(); i++)
            namesArr[i] = playerNames.get(i);

        boolean[] selectedItemsArr = new boolean[playerNames.size()];
        for(int i = 0; i < playerNames.size(); i++)
            selectedItemsArr[i] = mSelectedItems.contains(namesArr[i]);

        builder.setMultiChoiceItems(namesArr, selectedItemsArr,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        if (isChecked) {
                            mSelectedItems.add(playerNames.get(which));
                        } else if (mSelectedItems.contains(playerNames.get(which))) {
                            mSelectedItems.remove(playerNames.get(which));
                        }
                    }});
        return builder.create();
    }

    public void updatePlayersFromDb(){
        playerNames.clear();
        playerNames.addAll(Util.loadProfileNames(getContext()));
    }

    public ArrayList<String> getSelectedPlayers() {
        return mSelectedItems;
    }

}
