package com.fraz.dartlog;

import java.util.Arrays;

public class X01 {

    private String[] players;
    private Integer[] scores;
    private Integer currentPlayerIdx;

    public X01(String[] players, Integer x) {
        this.players = players;
        scores = new Integer[players.length];
        currentPlayerIdx = 0;

        Arrays.fill(scores, x*100 + 1);
    }

    public void enterScore(Integer score) {
        scores[currentPlayerIdx] -= score;
    }

    public String[] getPlayers() {
        return players;
    }

    public Integer[] getScores() {
        return scores;
    }

    public Integer getCurrentPlayerIdx() {
        return currentPlayerIdx;
    }

    public void next() {
        currentPlayerIdx = (currentPlayerIdx + 1) % players.length;
    }
}
