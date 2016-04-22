package com.fraz.dartlog;

import java.util.LinkedList;

public abstract class PlayerData {

    private String playerName;
    protected LinkedList<Integer> scoreHistory;
    protected LinkedList<Integer> totalScoreHistory;
    protected int score;

    public PlayerData(String playerName) {
        this.playerName = playerName;
    }

    public void initPlayerData(int score) {
        this.score = score;
        scoreHistory = new LinkedList<>();
        totalScoreHistory = new LinkedList<>();
    }

    public void submitScore(int achievedScore, int totalScore) {
        scoreHistory.add(achievedScore);
        totalScoreHistory.add(score);

        score = totalScore;
    }

    public void undo() {
        if (!scoreHistory.isEmpty())
            scoreHistory.removeLast();

        if (!totalScoreHistory.isEmpty())
            score = totalScoreHistory.removeLast();
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public LinkedList<Integer> getScoreHistory() {
        return scoreHistory;
    }

    public LinkedList<Integer> getTotalScoreHistory() { return totalScoreHistory; }
}
