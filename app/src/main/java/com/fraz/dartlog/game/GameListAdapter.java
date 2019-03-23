package com.fraz.dartlog.game;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fraz.dartlog.R;

import java.util.LinkedList;

public abstract class GameListAdapter<T extends GameListAdapter.ViewHolder> extends RecyclerView.Adapter<T> {

    protected Game game;

    public GameListAdapter(Game game) {
        this.game = game;
    }

    @Override
    public abstract T onCreateViewHolder(ViewGroup parent, int viewType);

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

    @Override
    public void onBindViewHolder(T holder, int position) {
        final PlayerData player = game.getPlayer(position);

        holder.playerName.setText(player.getPlayerName());
        holder.score.setText(String.valueOf(player.getScore()));

        setBackgroundColor(player, holder);

        // Set total score history text
        LinkedList<Integer> scores = new LinkedList<>(player.getScoreHistory());
        holder.totalScoreHistory.setText(createScoresString(scores));
    }

    private void setBackgroundColor(PlayerData player, ViewHolder holder) {
        if (game.isGameOver() && game.getWinner() == player) {
            holder.background_group.setBackgroundResource(R.color.accent);
            holder.itemView.setAlpha(1f);
            holder.playerName.setAlpha(1f);
            holder.current_player_indicator.setAlpha(0.0f);
            holder.playerName.setTypeface(null, Typeface.NORMAL);
        } else if (game.getCurrentPlayerIdx() == holder.getAdapterPosition()) {
            holder.itemView.setElevation(8);
            holder.itemView.setBackgroundResource(R.color.main_white);
            holder.itemView.setAlpha(1f);
            holder.playerName.setAlpha(1f);
            holder.background_group.setBackgroundResource(R.drawable.list_item);
            holder.current_player_indicator.setAlpha(1.0f);
            holder.playerName.setTypeface(null, Typeface.BOLD);
        } else {
            holder.itemView.setElevation(4);
            holder.itemView.setBackgroundResource(R.color.main_white);
            holder.itemView.setAlpha(.85f);
            holder.playerName.setAlpha(.75f);
            holder.background_group.setBackgroundResource(R.drawable.list_item);
            holder.current_player_indicator.setAlpha(0.0f);
            holder.playerName.setTypeface(null, Typeface.NORMAL);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView playerName;
        TextView score;
        TextView totalScoreHistory;
        ViewGroup background_group;
        View current_player_indicator;

        public ViewHolder(View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.game_player_list_item_name);
            score = itemView.findViewById(R.id.game_player_list_item_score);
            totalScoreHistory = itemView.findViewById(R.id.total_score_history);
            background_group = itemView.findViewById(R.id.game_player_list_item_background);
            current_player_indicator =
                    itemView.findViewById(R.id.game_player_list_item_current_player_indicator);
        }
    }
}