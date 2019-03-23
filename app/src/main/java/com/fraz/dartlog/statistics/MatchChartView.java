package com.fraz.dartlog.statistics;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.db.chart.model.ChartSet;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.fraz.dartlog.R;
import com.fraz.dartlog.game.PlayerData;

import java.util.LinkedList;

public class MatchChartView extends GameChart{

    public MatchChartView(Context context) {
        super(context, R.layout.match_chart);
    }

    public MatchChartView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.match_chart);
    }

    public void initMatchChart() {
        setTitle("Total score by turn");
        LineChartView matchChart = findViewById(R.id.line_chart);

        addGameData(matchChart);

        matchChart.setYAxis(false);
        matchChart.setXAxis(false);
        if (getGameData().getGameType().equals("x01")) {
            Paint halfLinePaint = new Paint();
            halfLinePaint.setColor(Color.BLACK);
            matchChart.setValueThreshold(50, 50, halfLinePaint);
        }
        matchChart.show();
    }

    public ChartSet getPlayerChartSet
            (PlayerData player, int color) {
        int matchTurns = getGameData().getTurns();
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
        return dataSet;
    }
}
