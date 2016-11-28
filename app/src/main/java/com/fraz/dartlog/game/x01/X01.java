package com.fraz.dartlog.game.x01;

import android.app.Activity;

import com.fraz.dartlog.game.Game;
import com.fraz.dartlog.game.PlayerData;

import java.io.Serializable;
import java.util.ArrayList;

public class X01 extends Game implements Serializable{

    X01(Activity context, ArrayList<? extends X01PlayerData> playerData) {
        super(context, playerData);
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

    public int getNextStartingPlayer() {
        return (getStartingPlayerIdx() + 1) % getNumberOfPlayers();
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

    void newLeg() {
        newGame();
    }

    public int getX() {
        return ((X01PlayerData)getPlayer(0)).getX();
    }

    public int getDoubleOutAttempts() {
        return ((X01PlayerData)getPlayer(0)).getDoubleOutAttempts();
    }
}
