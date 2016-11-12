package com.fraz.dartlog.game;

import java.util.LinkedList;

/**
 * Created by CarlR on 09/10/2016.
 */
public class AdditionScoreManager extends ScoreManager {

    private LinkedList<Integer> totalScoreHistory = new LinkedList<>();

    public boolean submitScore(int score) {
        int newScore = getScore() + score;

        totalScoreHistory.add(getScore());
        super.submitScore(score);
        this.score = newScore;
        return true;
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
        score = 0;
    }
}
