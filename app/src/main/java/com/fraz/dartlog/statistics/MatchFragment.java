package com.fraz.dartlog.statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fraz.dartlog.R;
import com.fraz.dartlog.game.GameData;

public class MatchFragment extends Fragment {
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
        View layout = inflater.inflate(R.layout.fragment_match, container, false);
        Bundle args = getArguments();
        MatchTableView matchTable = (MatchTableView) layout.findViewById(R.id.match_table);
        MatchChartView matchChart = (MatchChartView) layout.findViewById(R.id.match_chart);
        ScoreChartView scoreChart = (ScoreChartView) layout.findViewById(R.id.score_chart);
        gameData = (GameData) args.getSerializable(ARG_GAME_DATA);
        matchTable.setGame(gameData);
        matchChart.setGame(gameData);
        scoreChart.setGame(gameData);
        return layout;
    }
}
