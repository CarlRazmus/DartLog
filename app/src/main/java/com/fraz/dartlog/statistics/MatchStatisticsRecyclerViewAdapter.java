package com.fraz.dartlog.statistics;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.game.GameData;
import com.fraz.dartlog.game.PlayerData;

import java.util.LinkedList;
import java.util.Locale;

public class MatchStatisticsRecyclerViewAdapter extends RecyclerView.Adapter<
        MatchStatisticsRecyclerViewAdapter.ViewHolder> {

    private GameData game;
    private Context context;
    private boolean showTotal;

    public MatchStatisticsRecyclerViewAdapter(GameData game) {
        this.game = game;
        this.showTotal = true;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.match_statistics_scoreboard_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int rows = getRows();
        int row_idx = position % rows;
        int col_idx = position / rows;
        String text;
        PlayerData player = game.getPlayer(col_idx);

        if (row_idx == 0) {
            text = player.getPlayerName();
            holder.scoreView.setTextColor(Color.BLACK);
        }
        else {
            text = getScoreText(player, row_idx - 1);
            holder.scoreView.setTextColor(context.getResources().getColor(R.color.main_grey));
        }

        holder.scoreView.setTypeface(Typeface.DEFAULT);
        holder.scoreView.setText(text);
    }

    private String getScoreText(PlayerData player, int index) {
        LinkedList<Integer> scores = player.getTotalScoreHistory();
        if (index < scores.size())
            return String.format(
                    Locale.getDefault(),
                    "%s (%s)",
                    Integer.toString(scores.get(index)),
                    getTurnScore(player, index));
        else if (index == scores.size())
            return Integer.toString(player.getScore());
        else
            return "-";
    }

    private String getTurnScore(PlayerData player, int index) {
        LinkedList<Integer> scores = player.getScoreHistory();
        if (index < scores.size())
            return Integer.toString(scores.get(index));
        else
            return "-";
    }

    @Override
    public int getItemCount() {
        return getRows() * game.getNumberOfPlayers();
    }

    public void setShowTotalScore(boolean showTotal) {
        this.showTotal = showTotal;
        notifyDataSetChanged();
    }

    private int getRows() {
        return showTotal ? game.getTurns() + 2 : game.getTurns() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView scoreView;

        ViewHolder(View view) {
            super(view);
            scoreView = (TextView) view;
        }
    }
}
