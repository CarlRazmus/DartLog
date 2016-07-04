package com.fraz.dartlog.game.settings;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fraz.dartlog.R;

import java.util.ArrayList;
import java.util.List;


public class AvailablePlayersListAdapter extends ArrayAdapter<String> {

    private List<String> availablePlayers;
    private Activity context;
    private ArrayList<Integer> selectedIndexes;


    public AvailablePlayersListAdapter(Activity context, List<String> availablePlayers) {
        super(context, R.layout.participant_list_item, availablePlayers);

        this.context = context;
        this.availablePlayers = availablePlayers;

        selectedIndexes = new ArrayList<>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listItem = convertView;

        if (convertView == null) {
            listItem = inflater.inflate(R.layout.participant_list_item, parent, false);
        }

        TextView participantNameView = (TextView) listItem.findViewById(R.id.participant_name);
        participantNameView.setText(availablePlayers.get(position));

        return listItem;
    }
    public void toggleSelected(Integer idx)
    {
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
}
