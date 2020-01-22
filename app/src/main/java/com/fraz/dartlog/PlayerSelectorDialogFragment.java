package com.fraz.dartlog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

public class PlayerSelectorDialogFragment extends DialogFragment{

    public PlayerSelectorDialogListener mListener;

    private ArrayList<String> playerNames = new ArrayList<>();
    private ArrayList<String> selectedPlayerNames = new ArrayList<>();


    public interface PlayerSelectorDialogListener {
        void onDialogPositiveClick(PlayerSelectorDialogFragment dialog);
    }


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
        updatePlayersFromDb();

        boolean[] selectedItemsArr = new boolean[playerNames.size()];
        selectedPlayerNames.clear();
        selectedPlayerNames.addAll(getArguments().getStringArrayList("selectedNames"));
        for (String name : selectedPlayerNames){
            selectedItemsArr[playerNames.indexOf(name)] = true;
        }

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

        builder.setMultiChoiceItems(convertArrayListToArray(playerNames), selectedItemsArr,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        if (isChecked) {
                            selectedPlayerNames.add(playerNames.get(which));
                        } else if (selectedPlayerNames.contains(playerNames.get(which))) {
                            selectedPlayerNames.remove(playerNames.get(which));
                        }
                    }});
        return builder.create();
    }

    public void updatePlayersFromDb(){
        playerNames.clear();
        playerNames.addAll(Util.loadProfileNames());
    }

    public ArrayList<String> getSelectedPlayers() {
        return selectedPlayerNames;
    }

    private String[] convertArrayListToArray(ArrayList<String> arrayList){
        String[] convertedArray = new String[arrayList.size()];
        for(int i = 0; i < arrayList.size(); i++){
            convertedArray[i] = arrayList.get(i);
        }
        return convertedArray;
    }
}
