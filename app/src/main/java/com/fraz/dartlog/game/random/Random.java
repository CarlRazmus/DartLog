package com.fraz.dartlog.game.random;

import com.fraz.dartlog.game.Game;
import com.fraz.dartlog.game.PlayerData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Random extends Game implements Serializable{

    private final int nrOfTurns;
    private ArrayList<Integer> fields = new ArrayList<>();
    private java.util.Random rand;


    private void addFields(int nrOfTurns) {
        for(int i=0; i<nrOfTurns; i++)
            generateRandomFieldNr();
    }

    private void generateRandomFieldNr() {
       fields.add(rand.nextInt(20) + 1);
    }

    private void updateGameState() {
        nextPlayer();
        if (getTurn() == nrOfTurns + 1) {
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


    public Random(ArrayList<? extends PlayerData> playerData, int nrOfTurns) {
        super(playerData);
        this.nrOfTurns = nrOfTurns;
        rand = new java.util.Random();

        addFields(nrOfTurns);
    }

    public int getNextStartingPlayer() {
        return 0;
    }

    public int getCurrentField(){return fields.get(getTurn() - 1);}

    public int getNextField() {
        int turn = getTurn();
        if (fields.size() > turn) {
            return fields.get(turn);
        }
        return 0;
    }

    public int getNrOfTurns(){
        return nrOfTurns;
    }

    public void newLeg() {
        fields = new ArrayList<>();
        addFields(nrOfTurns);
        newGame();
    }

    public boolean submitScore(int score) {
        if (!isGameOver()) {
            super.submitScore(score);
            updateGameState();
        }
        return true;
    }
}
