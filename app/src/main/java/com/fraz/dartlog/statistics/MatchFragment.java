package com.fraz.dartlog.statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.game.GameData;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MatchFragment extends DialogFragment {
    public static final String ARG_GAME_DATA = "game_data";
    private GameData gameData;

    public static MatchFragment newInstance(GameData game) {
        MatchFragment matchFragment = new MatchFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_GAME_DATA, game);
        matchFragment.setArguments(args);

        return matchFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        NestedScrollView layout = (NestedScrollView) inflater.inflate(R.layout.fragment_match, container, false);

        // Enable focus of the scrollview so that children won't get focus initially.
        layout.setFocusableInTouchMode(true);
        layout.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);


        MatchTableView matchTable = (MatchTableView) layout.findViewById(R.id.match_table);
        MatchChartView matchChart = (MatchChartView) layout.findViewById(R.id.match_chart);
        ScoreChartView scoreChart = (ScoreChartView) layout.findViewById(R.id.score_chart);

        Bundle args = getArguments();
        gameData = (GameData) args.getSerializable(ARG_GAME_DATA);
        initHeader(layout);
        matchChart.setGame(gameData);
        matchTable.setGame(gameData);
        scoreChart.setGame(gameData);
        return layout;
    }

    private void initHeader(View layout) {
        TextView gameTypeTextView = (TextView) layout.findViewById(R.id.match_game_type);
        gameTypeTextView.setText(gameData.getGameType().toUpperCase());

        TextView winnerTextView = (TextView) layout.findViewById(R.id.match_winner);
        winnerTextView.setText(gameData.getWinner().getPlayerName());

        TextView dateTextView = (TextView) layout.findViewById(R.id.match_date);
        dateTextView.setText(new SimpleDateFormat("EEEE kk:mm, dd MMM yyyy", Locale.getDefault()).
                format(gameData.getDate().getTime()));
    }
}
