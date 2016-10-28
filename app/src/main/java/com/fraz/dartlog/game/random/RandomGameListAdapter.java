package com.fraz.dartlog.game.random;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.game.PlayerData;

import java.util.LinkedList;

/**
 * Created by CarlR on 09/10/2016.
 */
public class RandomGameListAdapter extends BaseExpandableListAdapter {

    private Random game;
    private Activity context;
    private final int colors[] = {R.color.primary_color, R.color.main_blue,
            R.color.main_green, R.color.main_yellow};


    public RandomGameListAdapter(Activity context, Random game) {
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
        PlayerData player = (PlayerData) game.getPlayer(position);

        if (view == null) {
            listItem = inflater.inflate(R.layout.game_player_list_item, parent, false);
        }

        TextView playerNameView = (TextView) listItem.findViewById(R.id.game_player_list_item_name);
        playerNameView.setText(player.getPlayerName());

        TextView scoreView = (TextView) listItem.findViewById(R.id.game_player_list_item_score);
        scoreView.setText(String.valueOf(player.getScore()));

        setBackgroundColor(player, position, listItem);
        return listItem;
    }

    @Override
    public View getChildView(int position, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listItem = view;

        if (view == null) {
            listItem = inflater.inflate(R.layout.game_player_list_item, parent, false);
        }
        PlayerData player = (PlayerData) game.getPlayer(position);

        // Set total score history text
        LinkedList<Integer> scores = new LinkedList<>(player.getTotalScoreHistory());
        scores.addLast(player.getScore());
        String totalScoreHistoryText = createScoresString(scores);
        ((TextView) listItem.findViewById(R.id.total_score_history)).setText(totalScoreHistoryText);

        return listItem;
    }

    private String createScoresString(LinkedList<Integer> scores) {
        String scoreHistoryText = "";
        for (Integer score : scores) {
            scoreHistoryText += String.format("%s ", Integer.toString(score));
        }
        return scoreHistoryText.trim();
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    private void setBackgroundColor(PlayerData player, int position, View listItem) {
        if (player.getScore() == 0) {
            listItem.setBackgroundResource(R.drawable.game_player_winner);
        } else if (game.getCurrentPlayerIdx() == position) {
            listItem.setBackgroundResource(R.drawable.game_player_active);
        } else {
            listItem.setBackgroundResource(R.drawable.game_player_normal);
        }
    }
}
