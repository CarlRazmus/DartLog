package com.fraz.dartlog;


public class PlayerData {

    private String playerName = null;
    private Integer score = null;


    PlayerData(String playerName, int score){
        this.playerName = playerName;
        this.score = score;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getScore() {
        return score;
    }
}
