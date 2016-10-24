package com.fraz.dartlog.game.x01;

import android.content.Context;

import com.fraz.dartlog.CheckoutChart;
import com.fraz.dartlog.R;
import com.fraz.dartlog.game.PlayerData;

import java.util.LinkedList;

public class X01PlayerData extends PlayerData {

    private CheckoutChart doubleCheckoutChart;
    private CheckoutChart singleCheckoutChart;
    private X01ScoreManager scoreManager;

    public X01PlayerData(Context context, String playerName, X01ScoreManager scoreManager) {
        super(playerName, scoreManager);
        doubleCheckoutChart = new CheckoutChart(context, R.raw.double_checkout_chart);
        singleCheckoutChart = new CheckoutChart(context, R.raw.single_checkout_chart);
        this.scoreManager = scoreManager;
    }

    public LinkedList<Integer> getTotalScoreHistory() {
        return scoreManager.getTotalScoreHistory();
    }

    public String getCheckoutText() {
        if (mustDoubleOut()) {
            return doubleCheckoutChart.getCheckoutText(getScore());
        } else {
            return singleCheckoutChart.getCheckoutText(getScore());
        }
    }

    public boolean mustDoubleOut() {
        return scoreManager.mustDoubleOut();
    }
}
