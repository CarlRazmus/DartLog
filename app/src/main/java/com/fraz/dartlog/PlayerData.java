package com.fraz.dartlog;

import java.util.LinkedList;

public class PlayerData {

    private String playerName;
    private int currentScore = -1;
    private LinkedList<Integer> scoreHistory = new LinkedList<>();
    private boolean active = false;

    public PlayerData(String playerName) {
        this.playerName = playerName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public void reset(int score) {
        scoreHistory = new LinkedList<>();
        active = false;
        setCurrentScore(score);
    }
}
