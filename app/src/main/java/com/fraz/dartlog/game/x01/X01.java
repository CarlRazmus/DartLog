package com.fraz.dartlog.game.x01;

import android.app.Activity;

import com.fraz.dartlog.CheckoutChart;
import com.fraz.dartlog.R;
import com.fraz.dartlog.game.Game;
import com.fraz.dartlog.game.PlayerData;
import com.fraz.dartlog.game.settings.GameData;

import java.io.Serializable;

public class X01 extends Game implements Serializable{

    private CheckoutChart checkoutChart;

    public X01(Activity context, GameData gameData) {
        super(context, gameData);

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
        PlayerData currentPlayer = getPlayer(currentPlayerIdx);
        if (currentPlayer.getScore() == 0) {
            gameData.setWinner(currentPlayer);
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
