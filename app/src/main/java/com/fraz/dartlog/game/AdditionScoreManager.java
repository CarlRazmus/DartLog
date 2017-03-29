package com.fraz.dartlog.game;

import java.util.LinkedList;

/**
 * Created by CarlR on 09/10/2016.
 */
public class AdditionScoreManager extends ScoreManager {

    public AdditionScoreManager()
    {
        score = 0;
    }

    public AdditionScoreManager(LinkedList<Integer> scoreHistory)
    {
        applyScores(scoreHistory);
    }

    public boolean submitScore(int score) {
        super.submitScore(score);
        this.score += score;
        return true;
    }

    @Override
    public void undoScore() {
        if (!totalScoreHistory.isEmpty()) {
            totalScoreHistory.removeLast();
            scoreHistory.removeLast();

            if (totalScoreHistory.isEmpty())
                score = 0;
            else
                score = totalScoreHistory.getLast();
        }
    }

    @Override
    public void reset() {
        super.reset();
        score = 0;
    }
}
