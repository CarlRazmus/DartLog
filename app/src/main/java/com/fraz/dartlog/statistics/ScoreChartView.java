package com.fraz.dartlog.statistics;

import android.content.Context;
import android.util.AttributeSet;

import com.db.chart.model.BarSet;
import com.db.chart.model.ChartSet;
import com.db.chart.view.BarChartView;
import com.fraz.dartlog.R;
import com.fraz.dartlog.game.PlayerData;

public class ScoreChartView extends GameChart{

    public ScoreChartView(Context context) {
        super(context, R.layout.match_chart);
    }

    public ScoreChartView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.score_chart);
    }

    public void initMatchChart() {
        setTitle("Scoring statistics");
        if(!getGameData().getGameType().equals("x01"))
            setVisibility(GONE);
        else {
            setVisibility(VISIBLE);

            BarChartView scoreChart = findViewById(R.id.bar_chart);

            addGameData(scoreChart);

            scoreChart.setYAxis(false);
            scoreChart.setXAxis(false);
            scoreChart.setBarSpacing(160f / scoreChart.getData().size());
            scoreChart.setRoundCorners(4f);
            scoreChart.setStep(1);
            scoreChart.show();
        }
    }

    public ChartSet getPlayerChartSet
            (PlayerData player, int color) {
        BarSet dataSet = new BarSet(new String[]{}, new float[]{});

        int lowScores = 0;
        int mediumScores = 0;
        int highScores = 0;
        int veryHighScores = 0;

        for (Integer score : player.getScoreHistory()) {
            if (score < 25)
                lowScores++;
            else if(score < 50)
                mediumScores++;
            else if(score < 75)
                highScores++;
            else
                veryHighScores++;
        }

        dataSet.addBar("0-24", lowScores);
        dataSet.addBar("25-49", mediumScores);
        dataSet.addBar("50-74", highScores);
        dataSet.addBar("75+", veryHighScores);

        dataSet.setColor(color);
        return dataSet;
    }
}
