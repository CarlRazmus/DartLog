package com.fraz.dartlog.game.setup;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fraz.dartlog.R;

import java.util.ArrayList;
import java.util.List;


public class AvailablePlayersRecyclerAdapter extends RecyclerView.Adapter<AvailablePlayersRecyclerAdapter.ViewHolder> {

    private List<String> availablePlayers;
    private ArrayList<Integer> selectedIndexes;


    public AvailablePlayersRecyclerAdapter(List<String> availablePlayers) {
        this.availablePlayers = availablePlayers;
        selectedIndexes = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return availablePlayers.size();
    }

    private void toggleSelected(Integer idx) {
        if(selectedIndexes.contains(idx))
        {
            selectedIndexes.remove(idx);
        }
        else
        {
            selectedIndexes.add(idx);
        }
    }

    private boolean isMarked(Integer idx){
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

        return new ViewHolder(listItem);
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
                notifyItemChanged(holder.getAdapterPosition());
            }
        }));

        if (isMarked(holder.getAdapterPosition()))
            listItemView.setBackgroundResource(R.color.accent);
        else
            listItemView.setBackgroundResource(android.R.color.transparent);
    }
}
