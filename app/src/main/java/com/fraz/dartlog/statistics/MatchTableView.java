package com.fraz.dartlog.statistics;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
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

    private void setTitle() {
        TextView titleView = (TextView) findViewById(R.id.player_match_statistics_title);
        titleView.setText(R.string.match_table_3_dart_score_title);
    }

    private void initializeScoreBoard(View layout) {
        GridLayout scoreboard = (GridLayout) layout.findViewById(R.id.match_statistics_scoreboard);
        scoreboard.setRowCount(getRows());
        scoreboard.setColumnCount(game.getNumberOfPlayers());
        fillScoreBoard(scoreboard);
    }

    private void fillScoreBoard(GridLayout scoreboard) {
        int rows = getRows();
        for (int i = 0; i < getItemCount(); i++) {
            int row_idx = i % rows;
            int col_idx = i / rows;
            PlayerData player = game.getPlayer(col_idx);

            View scoreView;

            if (row_idx == 0){
                scoreView = createPlayerNameView(scoreboard, player);
            } else {
                scoreView = createScoreView(scoreboard, row_idx - 1, player);
            }

            GridLayout.LayoutParams layoutParams =
                    (GridLayout.LayoutParams) scoreView.getLayoutParams();
            layoutParams.columnSpec = GridLayout.spec(col_idx, 1);
            layoutParams.rowSpec = GridLayout.spec(row_idx);
            if (row_idx == 0)
                layoutParams.setMargins(
                        layoutParams.leftMargin, 0,
                        layoutParams.rightMargin, layoutParams.bottomMargin);
            if (row_idx == (rows - 1))
                layoutParams.setMargins(
                        layoutParams.leftMargin, layoutParams.topMargin,
                        layoutParams.rightMargin, 0);
            if (col_idx == 0)
                layoutParams.setMargins(
                        0, layoutParams.topMargin,
                        layoutParams.rightMargin, layoutParams.bottomMargin);
            if (col_idx == (game.getNumberOfPlayers() -1))
                layoutParams.setMargins(
                        layoutParams.leftMargin, layoutParams.topMargin,
                        0, layoutParams.bottomMargin);

            scoreView.setLayoutParams(layoutParams);

            scoreboard.addView(scoreView, i);
        }
    }

    public View createPlayerNameView(GridLayout scoreboard, PlayerData player) {
        TextView playerNameView = (TextView) LayoutInflater.from(getContext())
                .inflate(R.layout.match_statistics_scoreboard_player_name, scoreboard, false);

        playerNameView.setText(player.getPlayerName());
        return playerNameView;
    }

    public View createScoreView(GridLayout scoreboard, int score_idx, PlayerData player) {
        View scoreView = LayoutInflater.from(getContext())
                .inflate(R.layout.match_statistics_scoreboard_score, scoreboard, false);

        TextView totalScoreView = (TextView) scoreView.findViewById(R.id.total_score);
        TextView turnScoreView = (TextView) scoreView.findViewById(R.id.turn_score);

        totalScoreView.setText(getScoreText(player, score_idx));
        turnScoreView.setText(getTurnScore(player, score_idx));
        return scoreView;
    }

    public void setGame(GameData game) {
        this.game = game;
        setTitle();
        initializeScoreBoard(layout);

        invalidate();
        requestLayout();
    }

    private String getScoreText(PlayerData player, int index) {
        LinkedList<Integer> scores = player.getTotalScoreHistory();
        if (index < scores.size())
            return Integer.toString(scores.get(index));
        else if (index == scores.size())
            return Integer.toString(player.getScore());
        else
            return "";
    }

    private String getTurnScore(PlayerData player, int index) {
        LinkedList<Integer> scores = player.getScoreHistory();
        if (index < scores.size())
            return Integer.toString(scores.get(index));
        else
            return "";
    }

    private int getItemCount() {
        return getRows() * game.getNumberOfPlayers();
    }

    private int getRows() {
        return game.getTurns() + 2;
    }
}
