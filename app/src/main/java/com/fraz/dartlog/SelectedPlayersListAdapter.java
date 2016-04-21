package com.fraz.dartlog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


public class SelectedPlayersListAdapter extends ArrayAdapter<String> {

    private List<String> playerNames;
    private Activity context;

    public SelectedPlayersListAdapter(Activity context, List<String> playerNames) {
        super(context, R.layout.game_player_list_item, playerNames);

        this.context = context;
        this.playerNames = playerNames;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listItem = convertView;

        if (convertView == null) {
            listItem = inflater.inflate(R.layout.select_players_list_item, parent, false);
        }

        TextView playerNameView = (TextView) listItem.findViewById(R.id.select_players_list_item_name);
        playerNameView.setText(playerNames.get(position));

        Button removeButton = (Button) listItem.findViewById(R.id.remove_player_button);
        removeButton.setText("X");

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(getItem(position));
                notifyDataSetChanged();
            }
        });

        return listItem;
    }
}
