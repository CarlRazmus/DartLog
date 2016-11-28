package com.fraz.dartlog.game;

import java.util.Collections;
import java.util.LinkedList;

public abstract class ScoreManager {

    protected LinkedList<Integer> scoreHistory = new LinkedList<>();
    protected LinkedList<Integer> totalScoreHistory = new LinkedList<>();
    protected int score;

    public ScoreManager() {}

    public boolean submitScore(int achievedScore) {
        scoreHistory.add(achievedScore);
        totalScoreHistory.add(getScore());
        return true;
    }

    public void undoScore() {
        if (!scoreHistory.isEmpty()) {
            scoreHistory.removeLast();
            score = totalScoreHistory.removeLast();
        }
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

    public void reset() {
        scoreHistory = new LinkedList<>();
        totalScoreHistory = new LinkedList<>();
    }

    public void applyScores(LinkedList<Integer> scoreHistory) {
        for(int score : scoreHistory)
        {
            submitScore(score);
        }
    }
}
