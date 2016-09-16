package com.fraz.dartlog.game;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.game.x01.X01;
import com.fraz.dartlog.game.x01.X01PlayerData;

import java.util.LinkedList;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder> {

    private X01 game;

    public GameListAdapter(Activity context, X01 game) {
        this.game = game;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.game_player_list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        X01PlayerData player = (X01PlayerData) game.getPlayer(position);

        holder.playerName.setText(player.getPlayerName());
        holder.score.setText(String.valueOf(player.getScore()));

        setBackgroundColor(player, position, holder.listItem);

        // Set total score history text
        LinkedList<Integer> scores = new LinkedList<>(player.getTotalScoreHistory());
        holder.totalScoreHistory.setText(createScoresString(scores));

        //Set checkout text
        holder.checkout.setText(game.getCheckoutText(player));
    }

    @Override
    public int getItemCount() {
        return game.getNumberOfPlayers();
    }

    private String createScoresString(LinkedList<Integer> scores) {
        String scoreHistoryText = "";
        for (Integer score : scores) {
            scoreHistoryText += String.format("%s ", Integer.toString(score));
        }
        return scoreHistoryText.trim();
    }

    private void setBackgroundColor(X01PlayerData player, int position, View listItem) {
        if (player.getScore() == 0) {
            listItem.setBackgroundResource(R.color.game_player_winner);
        } else if (game.getCurrentPlayerIdx() == position) {
            listItem.setBackgroundResource(R.color.light_grey);
        } else {
            listItem.setBackgroundResource(R.color.main_white);
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        View listItem;
        TextView playerName;
        TextView score;
        TextView totalScoreHistory;
        TextView checkout;

        public ViewHolder(View itemView) {
            super(itemView);
            listItem = itemView;
            playerName = (TextView) itemView.findViewById(R.id.game_player_list_item_name);
            score = (TextView) listItem.findViewById(R.id.game_player_list_item_score);
            totalScoreHistory = (TextView) listItem.findViewById(R.id.total_score_history);
            checkout = (TextView) listItem.findViewById(R.id.checkout);
        }
    }
}