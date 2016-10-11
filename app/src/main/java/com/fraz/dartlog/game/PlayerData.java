package com.fraz.dartlog.game;

import java.util.LinkedList;

public class PlayerData {

    private String playerName;
    private ScoreManager scoreManager;

    /**
     * Constructor used to initialize player data.
     * @param playerName  The name of the player.
     * @param scoreManager  An instance of the scoreManager class.
     */
    public PlayerData(String playerName, ScoreManager scoreManager) {
        this.playerName = playerName;
        this.scoreManager = scoreManager;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore()
    {
        return scoreManager.getScore();
    }

    public boolean submitScore(int score) { return scoreManager.submitScore(score); }

    public void undoScore() { scoreManager.undoScore(); }

    public int getMaxScore() { return scoreManager.getMaxScore(); }

    public float getAvgScore() { return scoreManager.getAvgScore(); }

    public LinkedList<Integer> getScoreHistory() { return scoreManager.getScoreHistory(); }

    public void resetScore() {
        scoreManager.reset();
    }
}
