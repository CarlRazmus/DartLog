package com.fraz.dartlog;

public class X01PlayerData extends PlayerData {

    public X01PlayerData(String playerName) {
        super(playerName);
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
