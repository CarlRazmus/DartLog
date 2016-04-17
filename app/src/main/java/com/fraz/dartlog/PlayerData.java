package com.fraz.dartlog;

import java.util.LinkedList;

public class PlayerData {

    private String playerName;
    private int currentScore;
    private LinkedList<Integer> scoreHistory;

    public PlayerData(String playerName) {
        this.playerName = playerName;
    }

    public void initPlayerData(int score) {
        currentScore = score;
        scoreHistory = new LinkedList<>();
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setCurrentScore(int score) {
        scoreHistory.add(currentScore);
        currentScore = score;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void undo() {
        if (!scoreHistory.isEmpty()) {
            currentScore = scoreHistory.removeLast();
        }
    }
}
