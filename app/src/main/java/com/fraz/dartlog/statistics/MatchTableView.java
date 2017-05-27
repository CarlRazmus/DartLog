package com.fraz.dartlog.statistics;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.game.GameData;

public class MatchTableView extends FrameLayout {

    private final View layout;
    private GameData game;
    private MatchStatisticsRecyclerViewAdapter adapter;

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
        RecyclerView scoreboard = (RecyclerView) layout.findViewById(R.id.match_statistics_scoreboard);

        adapter = new MatchStatisticsRecyclerViewAdapter(game);
        scoreboard.setAdapter(adapter);
        scoreboard.setLayoutManager(new GridLayoutManager(getContext(),
                game.getTurns() + 2, GridLayoutManager.HORIZONTAL, false));
        adapter.setShowTotalScore(true);
    }

    public void setGame(GameData game) {
        this.game = game;
        setTitle();
        initializeScoreBoard(layout);

        invalidate();
        requestLayout();
    }
}
