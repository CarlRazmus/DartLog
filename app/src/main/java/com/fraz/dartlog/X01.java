package com.fraz.dartlog;

import java.util.ArrayList;

public class X01 {

    private ArrayList<PlayerData> players;
    private Integer currentPlayerIdx;

    public X01(ArrayList<PlayerData> players, Integer x) {
        this.players = players;
        currentPlayerIdx = 0;

        for (PlayerData player : players) {
            player.setScore(x*100 + 1);
        }
    }

    public void enterScore(Integer score) {
        PlayerData currentPlayer = players.get(currentPlayerIdx);
        currentPlayer.setScore(currentPlayer.getScore() - score);
        nextPlayer();
    }

    private void nextPlayer() {
        currentPlayerIdx = (currentPlayerIdx + 1) % players.size();
    }
}
