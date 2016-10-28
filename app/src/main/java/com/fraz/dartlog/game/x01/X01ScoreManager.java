package com.fraz.dartlog.game.x01;

import com.fraz.dartlog.game.ScoreManager;

import java.util.LinkedList;

public class X01ScoreManager extends ScoreManager {

    private LinkedList<Integer> totalScoreHistory = new LinkedList<>();
    private int doubleOutsBeforeSingleOut = -1;

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
        if (hasBust(newScore)) {
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

    public Checkout getCurrentCheckoutType() {
        if (doubleOutsBeforeSingleOut == -1)
            return Checkout.DOUBLE;
        else if (getRemainingDoubleOutAttempts() > 0)
            return Checkout.DOUBLE_ATTEMPT;
        else
            return Checkout.SINGLE;
    }

    public boolean mustDoubleOut() {
        Checkout checkoutType = getCurrentCheckoutType();
        return checkoutType == Checkout.DOUBLE || checkoutType == Checkout.DOUBLE_ATTEMPT;
    }

    public int getRemainingDoubleOutAttempts() {
        if (doubleOutsBeforeSingleOut == -1)
            throw new UnsupportedOperationException("Attempts for double outs not used.");
        int remainingDoubleOutAttempts = doubleOutsBeforeSingleOut;
        for (Integer score : totalScoreHistory) {
            if (score <= 50) {
                remainingDoubleOutAttempts -= 1;
            }
        }
        return Math.max(0, remainingDoubleOutAttempts);
    }

    public void setDoubleOutsBeforeSingleOut(Integer doubleOutsBeforeSingleOut) {
        this.doubleOutsBeforeSingleOut = doubleOutsBeforeSingleOut;
    }

    private int getStartingScore()
    {
        return x * 100 + 1;
    }

    private boolean hasBust(int score) {
        return score < 0 || (score == 1 && mustDoubleOut());
    }

    public enum Checkout {
        SINGLE, DOUBLE, DOUBLE_ATTEMPT
    }
}
