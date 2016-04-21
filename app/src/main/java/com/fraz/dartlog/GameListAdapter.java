package com.fraz.dartlog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.LinkedList;

class GameListAdapter extends BaseExpandableListAdapter {

    private Game game;
    private Activity context;
    private final int colors[] = {R.color.main_red, R.color.main_blue,
                                  R.color.main_green, R.color.main_yellow};

    public GameListAdapter(Activity context, Game game) {
        this.context = context;
        this.game = game;
    }

    @Override
    public int getGroupCount() {
        return game.getNumberOfPlayers();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return game.getPlayer(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return game.getPlayer(i);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i * 100 + i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int position, boolean isExpanded, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listItem = view;
        X01PlayerData player = game.getPlayer(position);

        if (view == null) {
            listItem = inflater.inflate(R.layout.game_player_list_item, parent, false);
        }

        TextView playerNameView = (TextView) listItem.findViewById(R.id.game_player_list_item_name);
        playerNameView.setText(player.getPlayerName());

        TextView scoreView = (TextView) listItem.findViewById(R.id.game_player_list_item_score);
        scoreView.setText(String.valueOf(player.getScore()));

        setLineColor(position, listItem);
        setBackgroundColor(player, position, listItem);
        return listItem;
    }

    @Override
    public View getChildView(int position, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listItem = view;

        if (view == null) {
            listItem = inflater.inflate(R.layout.game_player_child_list_item, parent, false);
        }
        X01PlayerData player = game.getPlayer(position);

        String scoreHistoryText = createScoreHistoryString(player);
        TextView scoreHistoryView = (TextView) listItem.findViewById(R.id.score_history);
        scoreHistoryView.setText(scoreHistoryText);
        return listItem;
    }

    private String createScoreHistoryString(X01PlayerData player) {
        String scoreHistoryText = "";
        LinkedList<Integer> scoreHistory = player.getScoreHistory();
        for (int i = 0; i < scoreHistory.size(); i++) {
            Integer score = scoreHistory.get(i);
            scoreHistoryText += Integer.toString(score) + " ";
        }
        return scoreHistoryText.trim();
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    private void setBackgroundColor(X01PlayerData player, int position, View listItem) {
        if (player.getScore() == 0) {
            listItem.setBackgroundResource(R.drawable.game_player_winner);
        } else if (game.getCurrentPlayer() == position) {
            listItem.setBackgroundResource(R.drawable.game_player_active);
        } else {
            listItem.setBackgroundResource(R.drawable.game_player_normal);
        }
    }

    private void setLineColor(int position, View listItem) {
        View lineView = listItem.findViewById(R.id.game_player_list_line);
        lineView.setBackgroundResource(colors[position % colors.length]);
    }
}