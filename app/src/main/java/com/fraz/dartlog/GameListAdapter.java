package com.fraz.dartlog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class GameListAdapter extends BaseAdapter {

    private Game game;
    private Activity context;

    public GameListAdapter(Activity context, Game game) {
        this.context = context;
        this.game = game;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listItem = convertView;
        PlayerData player = game.getPlayer(position);

        if (convertView == null) {
            listItem = inflater.inflate(R.layout.game_player_list_item, parent, false);
        }

        TextView playerNameView = (TextView) listItem.findViewById(R.id.game_player_list_item_name);
        playerNameView.setText(player.getPlayerName());

        TextView scoreView = (TextView) listItem.findViewById(R.id.game_player_list_item_score);
        scoreView.setText(String.valueOf(player.getCurrentScore()));

        setBackgroundColor(player, listItem);
        return listItem;
    }

    @Override
    public int getCount() {
        return game.getNumberOfPlayers();
    }

    @Override
    public PlayerData getItem(int i) {
        return game.getPlayer(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private void setBackgroundColor(PlayerData player, View listItem) {
        if (player.getCurrentScore() == 0) {
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
