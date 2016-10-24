package com.fraz.dartlog.game.random;

import android.app.Activity;

import com.fraz.dartlog.CheckoutChart;
import com.fraz.dartlog.game.Game;
import com.fraz.dartlog.game.PlayerData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Random extends Game implements Serializable{

    private final int nrOfFields;
    private int nrOfFieldsCounter;
    private ArrayList<Integer> fields = new ArrayList<>();
    java.util.Random rand;


    public Random(Activity context, ArrayList<? extends PlayerData> playerData, int nrOfFields) {
        super(context, playerData);
        this.nrOfFields = nrOfFields;
        nrOfFieldsCounter = nrOfFields;
        rand = new java.util.Random();

        newField();
    }

    private void newField() {
        fields.add(0, rand.nextInt(20) + 1);
    }

    public boolean submitScore(int score) {
        if (!isGameOver()) {
            super.submitScore(score);
            updateGameState();
        }
        return true;
    }

    private void updateGameState() {
        PlayerData currentPlayer = getPlayer(currentPlayerIdx);

        nextPlayer();
        if (currentPlayerIdx == 0) {
            nrOfFieldsCounter -= 1;
            newField();
        }

        if (nrOfFieldsCounter == 0) {
            setWinner();
            showWinnerToast();
        }
    }

    private void setWinner() {
        setWinner(getPlayerWithHighestScore());
    }

    private PlayerData getPlayerWithHighestScore() {
        return Collections.max(getPlayers(), new Comparator<PlayerData>() {
            @Override
            public int compare(PlayerData playerData, PlayerData t1) {
                return playerData.getScore() - t1.getScore();
            }
        });
    }

    public int getCurrentField() {
        return fields.get(0);
    }

    public void newLeg() {
        newGame();
    }
}
