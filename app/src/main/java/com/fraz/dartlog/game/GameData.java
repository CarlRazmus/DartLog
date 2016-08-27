package com.fraz.dartlog.game;

import java.util.ArrayList;
import java.util.Calendar;

public class GameData {

    private PlayerData winner = null;
    private Calendar date;
    private ArrayList<? extends PlayerData> players;


    public GameData(ArrayList<? extends PlayerData> players, Calendar date,
                    PlayerData winner) {

        this.players = players;
        this.date = date;
        this.winner = winner;
    }

    public PlayerData getWinner() {
        return winner;
    }

    public PlayerData getPlayer(int index) {
        return players.get(index);
    }

    public PlayerData getPlayer(String name) {
        for (PlayerData player : players) {
            if (player.getPlayerName().equals(name))
                return player;
        }
        return null;
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public Calendar getDate()
    {
        return date;
    }
}
