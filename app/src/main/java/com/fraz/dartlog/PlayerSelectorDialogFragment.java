package com.fraz.dartlog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

public class PlayerSelectorDialogFragment extends DialogFragment{

    public interface PlayerSelectorDialogListener {
        void onDialogPositiveClick(PlayerSelectorDialogFragment dialog);
    }

    public PlayerSelectorDialogListener mListener;

    private ArrayList<String> playerNames = new ArrayList<>();
    private ArrayList<String> mSelectedItems;


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
        // Use the Builder class for convenient dialog construction
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

        mSelectedItems = new ArrayList<>();
        String[] names = new String[playerNames.size()];
        for(int i = 0; i < playerNames.size(); i++)
            names[i] = playerNames.get(i);

        builder.setMultiChoiceItems(names, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            mSelectedItems.add(playerNames.get(which));
                        } else if (mSelectedItems.contains(playerNames.get(which))) {
                            // Else, if the item is already in the array, remove it
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
