package com.fraz.dartlog.game.x01;

import com.fraz.dartlog.game.ScoreManager;

import java.util.LinkedList;

public class X01ScoreManager extends ScoreManager {

    /** The 'X' in X01 */
    private int x;

    public X01ScoreManager(int x, LinkedList<Integer> scoreHistory)
    {
        this(x);
        applyScores(scoreHistory);
    }

    public X01ScoreManager(int x) {
        this.x = x;
        score = getStartingScore();
    }

    public boolean submitScore(int score) {
        int newScore = getScore() - score;
        if (newScore < 0) {
            super.submitScore(0);
            return false;
        } else {
            super.submitScore(score);
            this.score = newScore;
            return true;
        }
    }

    @Override
    public void undoScore() {
        super.undoScore();
    }

    @Override
    public void reset() {
        super.reset();
        score = getStartingScore();
    }

    private int getStartingScore()
    {
        return x * 100 + 1;
    }
}
