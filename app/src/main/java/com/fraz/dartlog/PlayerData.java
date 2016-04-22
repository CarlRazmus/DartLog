package com.fraz.dartlog;

import java.util.Collections;
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

    public int getMaxScore() {
        if (!scoreHistory.isEmpty())
            return Collections.max(scoreHistory);
        else
            return 0;
    }

    public float getAvgScore() {
        int sum = 0;
        for (int score : scoreHistory) {
            sum += score;
        }

        if (!scoreHistory.isEmpty())
            return (float)sum / scoreHistory.size();
        else
            return 0;
    }

    public LinkedList<Integer> getScoreHistory() {
        return scoreHistory;
    }

    public LinkedList<Integer> getTotalScoreHistory() { return totalScoreHistory; }
}
