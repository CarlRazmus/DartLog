package com.fraz.dartlog.game;

import android.app.Activity;

import com.fraz.dartlog.CheckoutChart;
import com.fraz.dartlog.R;

import java.io.Serializable;
import java.util.ArrayList;

public class X01 extends Game implements Serializable{

    private CheckoutChart checkoutChart;

    public X01(Activity context, ArrayList<X01PlayerData> players, int x) {
        super(context, players, x*100 + 1);

        checkoutChart = new CheckoutChart(context, R.raw.checkout_chart);
        newGame();
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

    public String getCheckoutText(PlayerData player) {
        return checkoutChart.getCheckoutText(player.getScore());
    }

    private void updateGameState() {
        X01PlayerData currentPlayer = players.get(currentPlayerIdx);
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
        cycleStartingPlayer();
        newGame();
    }
}
