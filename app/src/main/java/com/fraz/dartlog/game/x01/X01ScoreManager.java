package com.fraz.dartlog.game.x01;

import com.fraz.dartlog.game.ScoreManager;

public class X01ScoreManager extends ScoreManager {

    private int doubleOutAttempts = -1;

    /** The 'X' in X01 */
    private int x;

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
        if (!scoreHistory.isEmpty()) {
            scoreHistory.removeLast();
            score = totalScoreHistory.removeLast();
        }
    }

    @Override
    public void reset() {
        super.reset();
        score = getStartingScore();
    }

    Checkout getCurrentCheckoutType() {
        if (doubleOutAttempts == -1)
            return Checkout.DOUBLE;
        else if (getRemainingDoubleOutAttempts() > 0)
            return Checkout.DOUBLE_ATTEMPT;
        else
            return Checkout.SINGLE;
    }

    private boolean mustDoubleOut() {
        Checkout checkoutType = getCurrentCheckoutType();
        return checkoutType == Checkout.DOUBLE || checkoutType == Checkout.DOUBLE_ATTEMPT;
    }

    int getRemainingDoubleOutAttempts() {
        if (doubleOutAttempts == -1)
            throw new UnsupportedOperationException("Attempts for double outs not used.");
        int remainingDoubleOutAttempts = doubleOutAttempts;
        for (Integer score : getTotalScoreHistory()) {
            if (score <= 50) {
                remainingDoubleOutAttempts -= 1;
            }
        }
        return Math.max(0, remainingDoubleOutAttempts);
    }

    public void setDoubleOutAttempts(int doubleOutAttempts) {
        this.doubleOutAttempts = doubleOutAttempts;
    }

    private int getStartingScore()
    {
        return x * 100 + 1;
    }

    private boolean hasBust(int score) {
        return score < 0 || (score == 1 && mustDoubleOut());
    }

    int getX() {
        return x;
    }

    int getDoubleOutAttempts() {
        return doubleOutAttempts;
    }

    enum Checkout {
        SINGLE, DOUBLE, DOUBLE_ATTEMPT
    }
}
