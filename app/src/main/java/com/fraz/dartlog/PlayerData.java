package com.fraz.dartlog;

import java.util.LinkedList;

public class PlayerData {

    private String playerName;
    private int score;
    private LinkedList<Integer> scoreHistory;

    public PlayerData(String playerName) {
        this.playerName = playerName;
    }

    public void initPlayerData(int score) {
        this.score = score;
        scoreHistory = new LinkedList<>();
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setScore(int score) {
        scoreHistory.add(this.score);
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void undo() {
        if (!scoreHistory.isEmpty()) {
            score = scoreHistory.removeLast();
        }
    }

    public LinkedList<Integer> getScoreHistory() {
        return scoreHistory;
    }
}
