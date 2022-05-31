package com.fraz.dartlog.view;

import android.app.Dialog;
import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.widget.EditText;
import com.fraz.dartlog.R;

import com.fraz.dartlog.viewmodel.ProfileListViewModel;

public class AddPlayerDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add new profile").setView(R.layout.dialog_add_player)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText profileNameEditText =
                                getDialog().findViewById(R.id.add_player_edit_text);
                        String name = profileNameEditText.getText().toString();
                        ProfileListViewModel profileListViewModel = ViewModelProviders.of(requireActivity()).get(ProfileListViewModel.class);
                        profileListViewModel.AddProfile(name);
                    }
                });
        return builder.create();
    }
}
