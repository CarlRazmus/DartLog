package com.fraz.dartlog.game.x01;

import android.app.Activity;

import com.fraz.dartlog.CheckoutChart;
import com.fraz.dartlog.R;
import com.fraz.dartlog.game.Game;
import com.fraz.dartlog.game.PlayerData;

import java.io.Serializable;
import java.util.ArrayList;

public class X01 extends Game implements Serializable{

    private CheckoutChart checkoutChart;

    public X01(Activity context, ArrayList<? extends PlayerData> playerData) {
        super(context, playerData);

        checkoutChart = new CheckoutChart(context, R.raw.checkout_chart);
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

    public boolean canCheckout(PlayerData player) {
        return checkoutChart.checkoutAvailable(player.getScore());
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
}
