package com.fraz.dartlog.statistics;

import android.content.Context;
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

public class MatchStatisticsRecyclerViewAdapter extends RecyclerView.Adapter<
        MatchStatisticsRecyclerViewAdapter.ViewHolder> {

    private GameData game;
    private Context context;

    public MatchStatisticsRecyclerViewAdapter(GameData game) {
        this.game = game;
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
        int row = position % game.getNumberOfPlayers();
        int column = position / game.getNumberOfPlayers();
        String text;
        text = getScore(row, column);
        holder.scoreView.setTypeface(Typeface.DEFAULT);
        holder.scoreView.setText(text);
    }

    private String getScore(int row, int column) {
        PlayerData player = game.getPlayer(row);
        LinkedList<Integer> scores = player.getTotalScoreHistory();
        if (column < scores.size())
            return Integer.toString(scores.get(column));
        else if (column == scores.size())
            return Integer.toString(player.getScore());
        else
            return "-";
    }

    @Override
    public int getItemCount() {
        int turns = game.getWinner().getTotalScoreHistory().size() + 1;
        return turns * game.getNumberOfPlayers();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView scoreView;

        ViewHolder(View view) {
            super(view);
            scoreView = (TextView) view;
        }
    }
}
