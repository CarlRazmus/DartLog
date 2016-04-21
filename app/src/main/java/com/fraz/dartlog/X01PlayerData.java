package com.fraz.dartlog;

import java.util.LinkedList;

public class X01PlayerData {

    private String playerName;
    private int score;
    private LinkedList<Integer> scoreHistory;

    public X01PlayerData(String playerName) {
        this.playerName = playerName;
    }

    public void initPlayerData(int score) {
        this.score = score;
        scoreHistory = new LinkedList<>();
    }

    public String getPlayerName() {
        return playerName;
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

    public int getScore() {
        return score;
    }

    public void undo() {
        if (!scoreHistory.isEmpty()) {
            score += scoreHistory.removeLast();
        }
    }

    public LinkedList<Integer> getScoreHistory() {
        return scoreHistory;
    }
}
