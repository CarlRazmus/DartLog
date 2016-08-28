package com.fraz.dartlog.game.setup;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.fraz.dartlog.R;

import java.util.ArrayList;
import java.util.List;


public class AvailablePlayersRecyclerAdapter extends RecyclerView.Adapter<AvailablePlayersRecyclerAdapter.ViewHolder> {

    private List<String> availablePlayers;
    private ArrayList<Integer> selectedIndexes;


    public AvailablePlayersRecyclerAdapter(Activity context, List<String> availablePlayers) {
        this.availablePlayers = availablePlayers;
        selectedIndexes = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return availablePlayers.size();
    }

    public void toggleSelected(Integer idx) {
        if(selectedIndexes.contains(idx))
        {
            selectedIndexes.remove(idx);
        }
        else
        {
            selectedIndexes.add(idx);
        }
    }

    public boolean isMarked(Integer idx){
        return selectedIndexes.contains(idx);
    }

    public ArrayList<String> getSelectedPlayers() {
        ArrayList<String> selectedPlayers = new ArrayList<>();

        for(Integer idx : selectedIndexes){
            selectedPlayers.add(availablePlayers.get(idx));
        }
        return selectedPlayers;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View listItemView;

        public ViewHolder(View listItem) {
            super(listItem);
            listItemView = listItem;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View listItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.available_player_list_item, parent, false);

        ViewHolder vh = new ViewHolder(listItem);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        TextView availablePlayerNameTextView = (TextView)holder.listItemView.findViewById(R.id.available_player_name);
        availablePlayerNameTextView.setText(availablePlayers.get(position));

        final View listItemView = holder.listItemView;
        listItemView.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toggleSelected(holder.getAdapterPosition());

                if (isMarked(holder.getAdapterPosition()))
                    v.setBackgroundResource(R.color.game_player_winner);
                else
                    v.setBackgroundResource(R.color.background_dark_transparent);
            }
        }));
    }
}
