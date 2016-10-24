package com.fraz.dartlog.game.random;

import com.fraz.dartlog.game.ScoreManager;

import java.util.ArrayList;
import java.util.LinkedList;

public class RandomScoreManager extends ScoreManager {


    public RandomScoreManager() {
        score = 0;
    }

    public RandomScoreManager(LinkedList<Integer> scoreHistory)
    {
        applyScores(scoreHistory);
    }

    public boolean submitScore(int score) {
        this.score += score;
        return true;
    }

    @Override
    public void undoScore() {
        super.undoScore();
    }

    @Override
    public void reset() {
        super.reset();
        score = 0;
    }
}
