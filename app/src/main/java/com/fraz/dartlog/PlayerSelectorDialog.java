package com.fraz.dartlog;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fraz.dartlog.databinding.AvailablePlayerListItemBinding;
import com.fraz.dartlog.util.BindingRecyclerViewHolder;
import com.fraz.dartlog.viewmodel.GameSetupViewModel;

public class PlayerSelectorDialog extends DialogFragment {

    private GameSetupViewModel setupViewModel;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setup_players_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupViewModel = ViewModelProviders.of(getActivity()).get(GameSetupViewModel.class);
        setupViewModel.setNewParticipantsToParticipants();
        recyclerView = view.findViewById(R.id.availablePlayersRecyclerView);
        recyclerView.setAdapter(new ParticipantsRecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        view.findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupViewModel.setParticipantsToNewParticipants();
                dismiss();
            }
        });
        view.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


    public class ParticipantsRecyclerViewAdapter extends RecyclerView.Adapter<BindingRecyclerViewHolder> {

        @Override
        @NonNull
        public BindingRecyclerViewHolder<AvailablePlayerListItemBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

            AvailablePlayerListItemBinding binding = AvailablePlayerListItemBinding.inflate(
                    layoutInflater, parent, false);

            return new BindingRecyclerViewHolder<>(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull final BindingRecyclerViewHolder holder, int position) {
            final AvailablePlayerListItemBinding binding = (AvailablePlayerListItemBinding) holder.getBinding();
            binding.setParticipant(setupViewModel.getNewParticipants().get(position));
        }

        @Override
        public int getItemCount() {
            return setupViewModel.getNewParticipants().size();
        }
    }
}
