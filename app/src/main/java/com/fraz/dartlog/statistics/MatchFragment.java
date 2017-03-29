package com.fraz.dartlog.statistics;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.db.chart.model.LineSet;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.fraz.dartlog.R;
import com.fraz.dartlog.Util;
import com.fraz.dartlog.game.GameData;
import com.fraz.dartlog.game.PlayerData;

import java.util.LinkedList;

public class MatchFragment extends Fragment {
    public static final String ARG_GAME_DATA = "game_data";
    private GameData gameData;
    private LineChartView matchChart;

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
        gameData = (GameData) args.getSerializable(ARG_GAME_DATA);
        matchTable.setGame(gameData);

        initMatchChart(layout);
        return layout;
    }

    private void initMatchChart(View layout) {
        matchChart = (LineChartView) layout.findViewById(R.id.match_chart);
        String[] playerNames = gameData.getPlayerNames();

        TypedArray colors = getResources().obtainTypedArray(R.array.chart_colors);

        for (int i = 0; i < playerNames.length; i++) {
            String playerName = playerNames[i];
            int color = colors.getColor(i % colors.length(), 0);
            addDataForPlayer(matchChart, gameData.getPlayer(playerName), gameData.getTurns(),
                    color);
            addPlayerButton(layout, i, playerName, color);
        }
        colors.recycle();
        matchChart.setYAxis(false);
        matchChart.setXAxis(false);
        if (gameData.getGameType().equals("x01")) {
            Paint halfLinePaint = new Paint();
            halfLinePaint.setColor(Color.BLACK);
            matchChart.setValueThreshold(50, 50, halfLinePaint);
        }
        matchChart.show();
    }

    private void addDataForPlayer(ChartView matchChart, PlayerData player, int matchTurns, int color) {
        LineSet dataSet = new LineSet(new String[]{}, new float[]{});
        LinkedList<Integer> totalScores = player.getTotalScoreHistory();
        int playedTurns = totalScores.size();
        for (int i = 0; i < playedTurns; i++) {
            dataSet.addPoint(String.valueOf(i), totalScores.get(i));
        }

        dataSet.addPoint(String.valueOf(totalScores.size()), player.getScore());
        if (matchTurns > playedTurns) {
            dataSet.addPoint(String.valueOf(matchTurns), player.getScore());
            dataSet.endAt(matchTurns);
        }
        dataSet.setThickness(6f);
        dataSet.setColor(color);
        dataSet.setSmooth(true);
        dataSet.setDotsColor(color);
        dataSet.setDotsRadius(8f);
        matchChart.addData(dataSet);
    }

    private void addPlayerButton(View layout, int playerIdx, String playerName, int color){
        GridLayout gridLayout = (GridLayout) layout.findViewById(R.id.match_chart_grid);
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setElevation(Util.pxFromDp(getContext(), 2));
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        int marginDp = (int) Util.pxFromDp(getContext(), 8);
        layoutParams.setMargins(marginDp, marginDp, marginDp, marginDp);
        layoutParams.columnSpec = GridLayout.spec(playerIdx % 3, 1f);
        layoutParams.rowSpec = GridLayout.spec(playerIdx / 3 + 1);
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundColor(color);
        textView.setTextColor(Color.WHITE);
        textView.setText(playerName);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        int paddingDp = (int) Util.pxFromDp(getContext(), 4);
        textView.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
        gridLayout.setRowCount(playerIdx / 3 + 2);
        gridLayout.addView(textView);
    }
}
