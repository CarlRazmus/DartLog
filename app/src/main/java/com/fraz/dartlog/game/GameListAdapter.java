package com.fraz.dartlog.game;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.game.x01.X01;
import com.fraz.dartlog.game.x01.X01PlayerData;

import java.util.LinkedList;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder> {

    private Game game;

    public GameListAdapter(Game game) {
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
        final PlayerData player = game.getPlayer(position);

        holder.playerName.setText(player.getPlayerName());
        holder.score.setText(String.valueOf(player.getScore()));

        setBackgroundColor(player, holder);

        // Set total score history text
        LinkedList<Integer> scores = new LinkedList<>(player.getTotalScoreHistory());
        holder.totalScoreHistory.setText(createScoresString(scores));

        updateCheckoutView(player, holder);
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

    private void setBackgroundColor(PlayerData player, ViewHolder holder) {
        if (player.getScore() == 0) {
            holder.background_group.setBackgroundResource(R.color.accent_color);
            holder.itemView.setAlpha(1f);
            holder.playerName.setAlpha(1f);
        } else if (game.getCurrentPlayerIdx() == holder.getAdapterPosition()) {
            holder.itemView.setElevation(8);
            holder.itemView.setBackgroundResource(R.color.main_white);
            holder.itemView.setAlpha(1f);
            holder.playerName.setAlpha(1f);
            holder.background_group.setBackgroundResource(R.drawable.list_item);
        } else {
            holder.itemView.setElevation(4);
            holder.itemView.setBackgroundResource(R.color.main_white);
            holder.itemView.setAlpha(.85f);
            holder.playerName.setAlpha(.75f);
            holder.background_group.setBackgroundResource(R.drawable.list_item);
        }
    }

    private void updateCheckoutView(X01PlayerData player, ViewHolder holder) {
        if (player.getScore() == 0) {
            holder.checkout_view.setVisibility(View.VISIBLE);
            holder.checkout.setText("");
            holder.checkoutLabel.setText(R.string.result_win);
            holder.current_player_indicator.setVisibility(View.GONE);
            holder.playerName.setTypeface(null, Typeface.NORMAL);
        }
        else {
            if (holder.getAdapterPosition() == game.getCurrentPlayerIdx()) {
                holder.current_player_indicator.setVisibility(View.VISIBLE);
                holder.playerName.setTypeface(null, Typeface.BOLD);
            } else {
                holder.current_player_indicator.setVisibility(View.GONE);
                holder.playerName.setTypeface(null, Typeface.NORMAL);
            }

            holder.checkout.setText(player.getCheckoutText());
            switch (player.getCurrentCheckoutType()) {
                case DOUBLE:
                    holder.checkoutLabel.setText(R.string.double_out);
                    break;
                case DOUBLE_ATTEMPT:
                    holder.checkoutLabel.setText(R.string.double_out_attempts);
                    String label = (String) holder.checkoutLabel.getText();
                    holder.checkoutLabel.setText(String.format(label,
                            player.getRemainingDoubleOutAttempts()));
                    break;
                case SINGLE:
                    holder.checkoutLabel.setText(R.string.single_out);
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView playerName;
        TextView score;
        TextView totalScoreHistory;
        TextView checkout;
        TextView checkoutLabel;
        ViewGroup checkout_view;
        ViewGroup background_group;
        View current_player_indicator;

        ViewHolder(View itemView) {
            super(itemView);
            playerName = (TextView) itemView.findViewById(R.id.game_player_list_item_name);
            score = (TextView) itemView.findViewById(R.id.game_player_list_item_score);
            totalScoreHistory = (TextView) itemView.findViewById(R.id.total_score_history);
            checkout = (TextView) itemView.findViewById(R.id.checkout);
            checkoutLabel = (TextView) itemView.findViewById(R.id.checkout_label);
            checkout_view = (ViewGroup) itemView.findViewById(R.id.checkout_view);
            background_group = (ViewGroup)
                    itemView.findViewById(R.id.game_player_list_item_background);
            current_player_indicator =
                    itemView.findViewById(R.id.game_player_list_item_current_player_indicator);
        }
    }
}