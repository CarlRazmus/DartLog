package com.fraz.dartlog;

import android.app.Activity;
import android.graphics.Color;
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

        if (items.get(position).isActive()) {
            listItem.setBackgroundColor(Color.parseColor("#455A64"));
        } else {
            listItem.setBackgroundColor(Color.parseColor("#37474F"));
        }
        return listItem;
    }
}
