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

    public X01(Activity context, ArrayList<X01PlayerData> players) {
        super(context, players);

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

    @Override
    protected void initPlayerData() {
        for(PlayerData player : players)
        {
            player.resetScore();
        }
    }

    public String getCheckoutText(PlayerData player) {
        return checkoutChart.getCheckoutText(player.getScore());
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
        cycleStartingPlayer();
        newGame();
    }
}
