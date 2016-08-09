package com.fraz.dartlog.game.x01;

import com.fraz.dartlog.game.PlayerData;

import java.util.LinkedList;

public class X01PlayerData extends PlayerData {

    public X01PlayerData(String playerName, X01ScoreManager playerScore) {
        super(playerName, playerScore);
    }

    public LinkedList<Integer> getTotalScoreHistory() {
        return ((X01ScoreManager)scoreManager).getTotalScoreHistory();
    }
}
