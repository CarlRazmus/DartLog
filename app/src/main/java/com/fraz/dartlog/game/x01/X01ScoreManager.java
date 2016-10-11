package com.fraz.dartlog.game.x01;

import com.fraz.dartlog.game.ScoreManager;

import java.util.LinkedList;

public class X01ScoreManager extends ScoreManager {

    private LinkedList<Integer> totalScoreHistory = new LinkedList<>();
    private Integer doubleOutsBeforeSingleOut = null;

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
            totalScoreHistory.add(getScore());
            return false;
        } else {
            totalScoreHistory.add(getScore());
            super.submitScore(score);
            this.score = newScore;
            return true;
        }
    }

    public LinkedList<Integer> getTotalScoreHistory() { return totalScoreHistory; }

    @Override
    public void undoScore() {
        super.undoScore();

        if (!totalScoreHistory.isEmpty())
            score = totalScoreHistory.removeLast();
    }

    @Override
    public void reset() {
        super.reset();
        totalScoreHistory = new LinkedList<>();
        score = getStartingScore();
    }

    public boolean mustDoubleOut() {
        if (doubleOutsBeforeSingleOut != null) {
            int doubleOutTries = 0;
            if(score <= 50)
                doubleOutTries += 1;
            for (Integer score : totalScoreHistory) {
                if (score <= 50) {
                    doubleOutTries += 1;
                }
            }
            return doubleOutTries <= doubleOutsBeforeSingleOut;
        } else {
            return true;
        }
    }

    public void setDoubleOutsBeforeSingleOut(Integer doubleOutsBeforeSingleOut) {
        this.doubleOutsBeforeSingleOut = doubleOutsBeforeSingleOut;
    }

    private int getStartingScore()
    {
        return x * 100 + 1;
    }
}
