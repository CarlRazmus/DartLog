package com.fraz.dartlog;

import java.util.LinkedList;

public abstract class PlayerData {

    private String playerName;
    protected LinkedList<Integer> scoreHistory;
    protected int score;

    public PlayerData(String playerName) {
        this.playerName = playerName;
    }

    public void initPlayerData(int score) {
        this.score = score;
        scoreHistory = new LinkedList<>();
    }

    public abstract boolean submitScore(int score);

    public abstract boolean undo();

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public LinkedList<Integer> getScoreHistory() {
        return scoreHistory;
    }
}
