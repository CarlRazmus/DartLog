package com.fraz.dartlog.game;

import java.util.LinkedList;

public class X01PlayerData extends PlayerData {

    public X01PlayerData(String playerName) {
        super(playerName);
    }

    public X01PlayerData(String playerName, LinkedList<Integer> playerScores) {
        super(playerName, playerScores);
    }

    public boolean submitScore(int score) {
        int newScore = getScore() - score;
        if (newScore < 0) {
            super.submitScore(0, getScore());
            return false;
        } else {
            super.submitScore(score, newScore);
            return true;
        }
    }

    public void undo() {
        super.undo();
    }
}
