package com.fraz.dartlog.game;

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

    /**
     * Constructor used to initialize player data with already known scores.
     * @param playerName    The name of the player.
     * @param scoreHistory  The list of scores the player achieved in order.
     */
    public PlayerData(String playerName, LinkedList<Integer> scoreHistory) {
        this.playerName = playerName;
        this.scoreHistory = scoreHistory;
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
