package com.fraz.dartlog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class PlayerListAdapter extends ArrayAdapter<PlayerData> {

    private List<PlayerData> items;
    private Activity context;

    public PlayerListAdapter(Activity context, List<PlayerData> items) {
        super(context, R.layout.game_player_list_item, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listItem = convertView;

        if (convertView == null) {
            listItem = inflater.inflate(R.layout.game_player_list_item, parent, false);
        }

        TextView playerNameView = (TextView) listItem.findViewById(R.id.game_player_list_item_name);
        playerNameView.setText(items.get(position).getPlayerName());

        TextView scoreView = (TextView) listItem.findViewById(R.id.game_player_list_item_score);
        scoreView.setText(String.valueOf(items.get(position).getScore()));

        setBackgroundColor(position, listItem);
        return listItem;
    }

    private void setBackgroundColor(int position, View listItem) {
        PlayerData player = items.get(position);
        if (player.getScore() == 0) {
            listItem.setBackgroundColor(
                    context.getResources().getColor(R.color.game_player_winner));
        } else if (player.isActive()) {
            listItem.setBackgroundColor(
                    context.getResources().getColor(R.color.game_player_highlight));
        } else {
            listItem.setBackgroundColor(context.getResources().getColor(R.color.background));
        }
    }
}
