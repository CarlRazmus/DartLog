package com.fraz.dartlog.statistics;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.db.chart.model.ChartSet;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.fraz.dartlog.R;
import com.fraz.dartlog.game.GameData;
import com.fraz.dartlog.game.PlayerData;

import java.util.ArrayList;
import java.util.LinkedList;

public class MatchChartView extends GridLayout{

    private GameData gameData;
    private LineChartView matchChart;
    private int showIdx = -1;

    public MatchChartView(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.match_chart, this);
    }

    public MatchChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.match_chart, this);
    }

    public MatchChartView(Context context, GameData gameData) {
        this(context);
        setGame(gameData);
    }

    public void initMatchChart() {
        matchChart = (LineChartView) findViewById(R.id.line_chart);
        GridLayout grid = (GridLayout) findViewById(R.id.match_chart_grid);
        String[] playerNames = gameData.getPlayerNames();

        TypedArray colors = getResources().obtainTypedArray(R.array.chart_colors);

        for (int i = 0; i < playerNames.length; i++) {
            String playerName = playerNames[i];
            int color = colors.getColor(i % colors.length(), 0);
            addDataForPlayer(gameData.getPlayer(playerName), gameData.getTurns(),
                    color);
            addPlayerButton(grid, i, playerName, color);
        }
        colors.recycle();
        matchChart.setYAxis(false);
        matchChart.setXAxis(false);
        if (gameData.getGameType().equals("x01")) {
            Paint halfLinePaint = new Paint();
            halfLinePaint.setColor(Color.BLACK);
            matchChart.setValueThreshold(50, 50, halfLinePaint);
        }
        if (showIdx == -1)
            matchChart.show();
        else
            matchChart.show(showIdx);
    }

    private void addDataForPlayer(PlayerData player, int matchTurns, int color) {
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

    private void addPlayerButton(GridLayout grid, final int playerIdx, String playerName, int color){
        Button button = (Button) LayoutInflater.from(getContext())
                .inflate(R.layout.chart_legend_label, grid, false);
        GridLayout.LayoutParams layoutParams = (LayoutParams) button.getLayoutParams();
        layoutParams.columnSpec = GridLayout.spec(playerIdx % 3, 1f);
        layoutParams.rowSpec = GridLayout.spec(playerIdx / 3 + 1);
        button.setLayoutParams(layoutParams);
        button.setBackgroundTintList(ColorStateList.valueOf(color));
        button.setText(playerName);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ChartSet> data = matchChart.getData();
                for (int i = 0; i < data.size(); i++) {
                    LineSet lineSet = (LineSet) data.get(i);
                    if (showIdx != playerIdx && i != playerIdx) {
                        lineSet.setVisible(false);
                    } else {
                        lineSet.setVisible(true);
                    }
                }
                showIdx = showIdx == playerIdx ? -1 : playerIdx;
                matchChart.notifyDataUpdate();
            }
        });
        setRowCount(playerIdx / 3 + 2);
        grid.addView(button);
    }

    public void setGame(GameData gameData) {
        this.gameData = gameData;
        initMatchChart();
        invalidate();
        requestLayout();
    }
}
