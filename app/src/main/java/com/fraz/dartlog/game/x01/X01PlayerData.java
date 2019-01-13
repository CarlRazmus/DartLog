package com.fraz.dartlog.game.x01;

import com.fraz.dartlog.CheckoutChart;
import com.fraz.dartlog.game.PlayerData;

import java.util.LinkedList;

public class X01PlayerData extends PlayerData {

    private CheckoutChart checkoutChart;
    private X01ScoreManager scoreManager;

    public X01PlayerData(CheckoutChart checkoutChart, String playerName, X01ScoreManager scoreManager) {
        super(playerName, scoreManager);
        this.scoreManager = scoreManager;
        this.checkoutChart = checkoutChart;
    }

    public LinkedList<Integer> getTotalScoreHistory() {
        return scoreManager.getTotalScoreHistory();
    }

    String getCheckoutText() {
        if (mustDoubleOut()) {
            return checkoutChart.getDoubleOutCheckoutText(getScore());
        } else {
            return checkoutChart.getSingleOutCheckoutText(getScore());
        }
    }

    X01ScoreManager.Checkout getCurrentCheckoutType() {
        return scoreManager.getCurrentCheckoutType();
    }

    int getRemainingDoubleOutAttempts() {
        return scoreManager.getRemainingDoubleOutAttempts();
    }

    private boolean mustDoubleOut() {
        X01ScoreManager.Checkout checkoutType = getCurrentCheckoutType();
        return checkoutType == X01ScoreManager.Checkout.DOUBLE ||
                checkoutType == X01ScoreManager.Checkout.DOUBLE_ATTEMPT;
    }

    int getX() {
        return scoreManager.getX();
    }

    int getDoubleOutAttempts() {
        return scoreManager.getDoubleOutAttempts();
    }
}
