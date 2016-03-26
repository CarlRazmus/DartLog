package com.fraz.dartlog;


public class PlayerData {

    private String playerName = null;
    private Integer score = null;
    private Boolean active = false;

    PlayerData(String playerName, int score){
        this.playerName = playerName;
        this.score = score;
    }

    public PlayerData(String playerName) {
        this.playerName = playerName;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
