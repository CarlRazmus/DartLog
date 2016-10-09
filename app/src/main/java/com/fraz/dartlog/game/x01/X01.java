package com.fraz.dartlog.game.x01;

import android.app.Activity;

import com.fraz.dartlog.CheckoutChart;
import com.fraz.dartlog.R;
import com.fraz.dartlog.game.Game;
import com.fraz.dartlog.game.PlayerData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class X01 extends Game implements Serializable{

    private static final Integer DEFAULT_DOUBLE_OUTS_BEFORE_SINGLE_OUT = 5;

    private CheckoutChart doubleCheckoutChart;
    private CheckoutChart singleCheckoutChart;
    private Integer doubleOutsBeforeSingleOut;

    public X01(Activity context, ArrayList<? extends X01PlayerData> playerData) {
        super(context, playerData);

        doubleCheckoutChart = new CheckoutChart(context, R.raw.double_checkout_chart);
        singleCheckoutChart = new CheckoutChart(context, R.raw.single_checkout_chart);
        this.doubleOutsBeforeSingleOut = DEFAULT_DOUBLE_OUTS_BEFORE_SINGLE_OUT;
    }

    public boolean submitScore(int score) {
        if (!isGameOver()) {
            if (!super.submitScore(score)) {
                showBustToast();
            }
            updateGameState();
        }
        return true;
    }

    public String getCheckoutText(X01PlayerData player) {
        if (isDoubleOut(player)) {
            return doubleCheckoutChart.getCheckoutText(player.getScore());
        } else {
            return singleCheckoutChart.getCheckoutText(player.getScore());
        }
    }

    public boolean isDoubleOut(X01PlayerData player) {
        if (doubleOutsBeforeSingleOut != null) {
            LinkedList<Integer> scoreHistory = player.getTotalScoreHistory();
            int doubleOutTries = 0;
            if(player.getScore() <= 50)
                doubleOutTries += 1;
            for (Integer score : scoreHistory) {
                if (score <= 50) {
                    doubleOutTries += 1;
                }
            }
            return doubleOutTries <= doubleOutsBeforeSingleOut;
        } else {
            return true;
        }
    }

    private void updateGameState() {
        PlayerData currentPlayer = getPlayer(currentPlayerIdx);
        if (currentPlayer.getScore() == 0) {
            setWinner(currentPlayer);
            showWinnerToast();
        }
        else {
            nextPlayer();
        }
    }

    private void showBustToast() {
        CharSequence text = "Bust!";
        showToast(text);
    }

    public void newLeg() {
        newGame();
    }

    public void setDoubleOutsBeforeSingleOut(Integer doubleOutsBeforeSingleOut) {
        this.doubleOutsBeforeSingleOut = doubleOutsBeforeSingleOut;
    }
}
