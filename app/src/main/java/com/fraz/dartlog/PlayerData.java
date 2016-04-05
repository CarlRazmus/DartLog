package com.fraz.dartlog;

public class PlayerData {

    private String playerName;
    private int score = -1;
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

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
