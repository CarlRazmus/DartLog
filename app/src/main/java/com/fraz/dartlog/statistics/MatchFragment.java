package com.fraz.dartlog.statistics;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.db.chart.model.LineSet;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.fraz.dartlog.R;
import com.fraz.dartlog.game.GameData;
import com.fraz.dartlog.game.PlayerData;

import java.util.LinkedList;

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
        LinearLayout layout =
                (LinearLayout) inflater.inflate(R.layout.fragment_match, container, false);
        Bundle args = getArguments();
        MatchTableView matchTable = (MatchTableView) layout.findViewById(R.id.match_table);
        gameData = (GameData) args.getSerializable(ARG_GAME_DATA);
        matchTable.setGame(gameData);

        initMatchChart(layout);
        return layout;
    }

    private void initMatchChart(LinearLayout layout) {
        LineChartView matchChart = (LineChartView) layout.findViewById(R.id.match_chart);
        String[] playerNames = gameData.getPlayerNames();

        TypedArray colors = getResources().obtainTypedArray(R.array.chart_colors);

        for (int i = 0; i < playerNames.length; i++) {
            String playerName = playerNames[i];
            addDataForPlayer(matchChart, gameData.getPlayer(playerName),
                    colors.getColor(i % colors.length(), 0));
        }
        colors.recycle();
        matchChart.show();
    }

    private void addDataForPlayer(ChartView matchChart, PlayerData player, int color) {
        LineSet dataSet = new LineSet(new String[]{}, new float[]{});
        LinkedList<Integer> totalScores = player.getTotalScoreHistory();
        for (int i = 0; i < totalScores.size(); i++) {
            dataSet.addPoint(String.valueOf(i), totalScores.get(i));
        }

        String lastTurnLabel = String.valueOf(totalScores.size());
        dataSet.addPoint(lastTurnLabel , player.getScore());
        dataSet.setThickness(6f);
        dataSet.setColor(color);
        dataSet.setSmooth(true);
        matchChart.addData(dataSet);
    }
}
