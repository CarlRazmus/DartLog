package com.fraz.dartlog;

public class X01PlayerData extends PlayerData {

    public X01PlayerData(String playerName) {
        super(playerName);
    }

    public boolean submitScore(int score) {
        int newScore = getScore() - score;
        if (newScore < 0) {
            scoreHistory.add(0);
            return false;
        } else {
            this.score = newScore;
            scoreHistory.add(score);
            return true;
        }
    }

    public boolean undo() {
        if (!scoreHistory.isEmpty()) {
            score += scoreHistory.removeLast();
            return true;
        }
        return false;
    }
}
