package com.fraz.dartlog.statistics;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.game.GameData;
import com.fraz.dartlog.game.PlayerData;

import java.util.LinkedList;

public class MatchTableView extends FrameLayout {

    private final View layout;
    private GameData game;

    public MatchTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        layout = inflater.inflate(R.layout.match_statistics, this);
    }

    public MatchTableView(Context context) {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        layout = inflater.inflate(R.layout.match_statistics, this);
    }

    public MatchTableView(Context  context, GameData game){
        this(context);
        setGame(game);
    }

    private void initializeScoreBoard(View layout) {
        RecyclerView scoreboard = layout.findViewById(R.id.match_statistics_scoreboard);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(
                getContext(), getRows(), GridLayoutManager.HORIZONTAL, false);
        scoreboard.setLayoutManager(layoutManager);
        MatchTableAdapter adapter = new MatchTableAdapter();
        scoreboard.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void setGame(GameData game) {
        this.game = game;
        initializeScoreBoard(layout);
    }

    private int getRows() {
        return game.getTurns() + 2;
    }

    private int getRow(int position) { return position % getRows(); }

    private int getColumn(int position) {
        return  position / (getRows());
    }

    private class MatchTableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private static final int NAME_VIEW_TYPE = 0;
        private static final int SCORE_VIEW_TYPE = 1;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == NAME_VIEW_TYPE){
                View nameView = LayoutInflater.from(getContext())
                        .inflate(R.layout.match_statistics_scoreboard_player_name, parent, false);
                return new NameViewHolder(nameView);
            } else {
                View scoreView = LayoutInflater.from(getContext())
                        .inflate(R.layout.match_statistics_scoreboard_score, parent, false);
                return new ScoreViewHolder(scoreView);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == NAME_VIEW_TYPE)
                onBindNameViewHolder((NameViewHolder) holder, getPlayer(position));
            else
                onBindScoreViewHolder(
                        (ScoreViewHolder) holder,
                        getPlayer(position),
                        getTurn(position));
        }

        private void onBindNameViewHolder(NameViewHolder holder, PlayerData player) {
            ((TextView)holder.itemView).setText(player.getPlayerName());
        }

        private void onBindScoreViewHolder(ScoreViewHolder holder, PlayerData player, int turnIdx) {
            holder.turnScoreView.setText(getTurnScore(player, turnIdx));
            holder.totalScoreView.setText(getTotalScoreText(player, turnIdx));
        }

        @Override
        public int getItemViewType(int position) {
            if (getRow(position) == 0)
                return NAME_VIEW_TYPE;
            else
                return SCORE_VIEW_TYPE;
        }

        @Override
        public int getItemCount() {
            return  getRows() * game.getNumberOfPlayers();
        }

        private String getTurnScore(PlayerData player, int index) {
            LinkedList<Integer> scores = player.getScoreHistory();
            if (index < scores.size())
                return Integer.toString(scores.get(index));
            else
                return "";
        }

        private String getTotalScoreText(PlayerData player, int index) {
            LinkedList<Integer> scores = player.getTotalScoreHistory();
            if (index < scores.size())
                return Integer.toString(scores.get(index));
            else if (index == scores.size())
                return Integer.toString(player.getScore());
            else
                return "";
        }

        private PlayerData getPlayer(int position) {
            return game.getPlayer(getColumn(position));
        }

        private int getTurn(int position) {
            return getRow(position) - 1;
        }

        class NameViewHolder extends RecyclerView.ViewHolder{
            NameViewHolder(View itemView) {
                super(itemView);
            }
        }

        class ScoreViewHolder extends RecyclerView.ViewHolder{
            final TextView totalScoreView;
            final TextView turnScoreView;

            ScoreViewHolder(View itemView) {
                super(itemView);
                totalScoreView = itemView.findViewById(R.id.total_score);
                turnScoreView = itemView.findViewById(R.id.turn_score);
            }
        }
    }
}
