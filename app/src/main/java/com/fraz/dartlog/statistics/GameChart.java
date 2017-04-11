package com.fraz.dartlog.statistics;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.db.chart.model.ChartSet;
import com.db.chart.view.ChartView;
import com.fraz.dartlog.R;
import com.fraz.dartlog.game.GameData;
import com.fraz.dartlog.game.PlayerData;

import java.util.ArrayList;

public abstract class GameChart extends GridLayout {

    private GameData gameData;
    private int showIdx = -1;
    private TextView titleView;

    public GameChart(Context context, int layoutId) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(layoutId, this);
        titleView = (TextView) findViewById(R.id.chart_title);
    }

    public GameChart(Context context, AttributeSet attrs, int layoutId) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(layoutId, this);
        titleView = (TextView) findViewById(R.id.chart_title);
    }

    public GameData getGameData() {
        return gameData;
    }

    public void addGameData(ChartView chartView) {
        GridLayout grid = (GridLayout) findViewById(R.id.match_chart_grid);
        String[] playerNames = gameData.getPlayerNames();

        TypedArray colors = getResources().obtainTypedArray(R.array.chart_colors);

        for (int i = 0; i < playerNames.length; i++) {
            String playerName = playerNames[i];
            int color = colors.getColor(i % colors.length(), 0);
            chartView.addData(
                    getPlayerChartSet(gameData.getPlayer(playerName), color));
            addPlayerButton(chartView, grid, i, playerName, color);
        }
        colors.recycle();
    }

    private void addPlayerButton(ChartView chartView, GridLayout grid, int playerIdx,
                                 String playerName, int color){
        Button button = (Button) LayoutInflater.from(getContext())
                .inflate(R.layout.chart_legend_label, grid, false);
        GridLayout.LayoutParams layoutParams = (LayoutParams) button.getLayoutParams();
        layoutParams.columnSpec = GridLayout.spec(playerIdx % 3, 1f);
        layoutParams.rowSpec = GridLayout.spec(playerIdx / 3 + 3);
        button.setLayoutParams(layoutParams);
        button.setBackgroundTintList(ColorStateList.valueOf(color));
        button.setText(playerName);
        setRowCount(playerIdx / 3 + 3);
        grid.addView(button);
        button.setOnClickListener(new LegendLabelOnClickListener(chartView, playerIdx));
    }

    public void setGame(GameData gameData) {
        this.gameData = gameData;
        initMatchChart();
        invalidate();
        requestLayout();
    }

    private class LegendLabelOnClickListener implements OnClickListener
    {
        private ChartView chartView;
        private int legendIdx;

        LegendLabelOnClickListener(ChartView chartView, int legendIdx) {

            this.chartView = chartView;
            this.legendIdx = legendIdx;
        }

        @Override
        public void onClick(View view) {
            ArrayList<ChartSet> data = chartView.getData();
            for (int i = 0; i < data.size(); i++) {
                ChartSet chartSet = data.get(i);
                if (showIdx != legendIdx && i != legendIdx) {
                    chartSet.setVisible(false);
                } else {
                    chartSet.setVisible(true);
                }
            }
            showIdx = showIdx == legendIdx ? -1 : legendIdx;
            chartView.notifyDataUpdate();
        }
    }

    public void setTitle(String title)
    {
        titleView.setText(title);
    }

    protected abstract ChartSet getPlayerChartSet(PlayerData player, int color);

    protected abstract void initMatchChart();
}
