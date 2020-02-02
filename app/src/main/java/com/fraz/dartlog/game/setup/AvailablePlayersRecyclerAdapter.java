package com.fraz.dartlog.game.setup;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.fraz.dartlog.R;

import java.util.ArrayList;
import java.util.Collections;


public class AvailablePlayersRecyclerAdapter extends RecyclerView.Adapter<AvailablePlayersRecyclerAdapter.ViewHolder> {

    private ArrayList<String> availablePlayers;
    private ArrayList<Integer> selectedIndexes = new ArrayList<>();


    public AvailablePlayersRecyclerAdapter(){
        availablePlayers = new ArrayList();
    }

    public void setAvailablePlayers(ArrayList<String> playersList){
        this.availablePlayers = playersList;
    }

    public void setSelectedPlayers(ArrayList<String> playerNames){
        selectedIndexes.clear();
        for(String name : playerNames)
            selectedIndexes.add(availablePlayers.indexOf(name));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return availablePlayers.size();
    }

    private void toggleSelected(Integer idx) {
        if (selectedIndexes.contains(idx))
            selectedIndexes.remove(idx);
        else
            selectedIndexes.add(idx);
    }

    public ArrayList<String> getSelectedPlayers() {
        ArrayList<String> selectedPlayers = new ArrayList<>();
        Collections.sort(selectedIndexes);

        for(Integer idx : selectedIndexes){
            selectedPlayers.add(availablePlayers.get(idx));
        }
        return selectedPlayers;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox statusCheckBox;
        public TextView nameTextView;

        public ViewHolder(View listItem) {
            super(listItem);
            statusCheckBox = listItem.findViewById(R.id.checkbox);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.available_player_list_item, parent, false);

        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.nameTextView.setText(availablePlayers.get(position));

        final CheckBox checkBox = holder.statusCheckBox;
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(selectedIndexes.contains(position));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleSelected(holder.getAdapterPosition());
            }
        });

        final View listItemView = holder.itemView;
        listItemView.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.toggle();
            }
        }));
    }
}
