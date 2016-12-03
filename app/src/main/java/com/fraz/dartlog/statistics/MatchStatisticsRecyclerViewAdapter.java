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

public class MatchStatisticsRecyclerViewAdapter extends RecyclerView.Adapter<
        MatchStatisticsRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private GameData game;

    public MatchStatisticsRecyclerViewAdapter(Context context, GameData game) {
        this.context = context;
        this.game = game;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_statistics_scoreboard_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int row = position % (game.getNumberOfPlayers() + 1);
        int column = position / (game.getNumberOfPlayers() + 1);
        String text;
        if (row == 0) {
            text = Integer.toString(column + 1);
            holder.scoreView.setTypeface(Typeface.DEFAULT_BOLD);
        }
        else {
            text = getScore(row, column);
            holder.scoreView.setTextColor(Color.BLACK);
            holder.scoreView.setTypeface(Typeface.DEFAULT);
        }
        holder.scoreView.setText(text);
    }

    private String getScore(int row, int column) {
        PlayerData player = game.getPlayer(row - 1);
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
        return turns + turns * game.getNumberOfPlayers();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView scoreView;

        ViewHolder(View view) {
            super(view);
            scoreView = (TextView) view;
        }
    }
}
